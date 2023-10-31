package crawler.util;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import crawler.Constants;
import crawler.exception.NovelConnectException;
import crawler.exception.NovelNotFoundException;

/**
 * 小説の情報のUtilityクラス.
 */
public class NovelManagerUtil {

    /** ログ出力クラス */
    private static final Logger log = LogManager.getLogger(NovelManagerUtil.class);

    /** 一時停止時間 */
    public static final long DELAY_ACCESS_TIME = Long.parseLong(Constants.RB.getString("delay.access.time")) * 1000;

    /** プロキシ設定 */
    public static final String PROXY = System.getProperty("http_proxy");

    /** エラーメッセージテンプレート */
    public static final String ERROR_MSG_TMPL = "url:{}";

    /**
     * プライベート・コンストラクタ.
     * Utilityクラスはインスタンス化禁止.
     */
    private NovelManagerUtil() {
    }

    /**
     * 文字列からURLオブジェクトを生成する.
     *
     * @param url
     *            URL
     * @return URLオブジェクト
     * @throws NovelNotFoundException
     *             URLで指定されたコンテンツが見つからない
     */
    public static URL getUrl(final String url) throws NovelNotFoundException {
        try {
            return URI.create(url).toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            log.error(ERROR_MSG_TMPL, url, e);
            throw new NovelNotFoundException();
        }
    }

    /**
     * htmlファイルを取得する.
     *
     * @param url
     *            URLオブジェクト
     * @return htmlファイル
     * @throws NovelNotFoundException
     *             URLで指定されたコンテンツが見つからない
     */
    public static Document getSource(final String url) throws NovelNotFoundException {
        // ネットワーク負荷低減のため、実行を一時停止
        delayAccess();

        try {
            if (url.startsWith(Constants.LOCAL_FILE_PREFIX)) {
                return Jsoup.parse(new File(url.substring(Constants.LOCAL_FILE_PREFIX.length())), Constants.ENCODING.name());
            } else {
                Connection conn = Jsoup.connect(url);

                if (StringUtils.isNotEmpty(PROXY)) {
                    var proxyUrl = URI.create(PROXY).toURL();
                    // プロキシ設定有りの場合
                    conn = conn.proxy(proxyUrl.getHost(), proxyUrl.getPort());

                    if (StringUtils.isNotEmpty(proxyUrl.getUserInfo())) {
                        // プロキシの認証有りの場合
                        conn = conn.header("Authorization",
                                Base64.getEncoder().encodeToString(proxyUrl.getUserInfo().getBytes(Constants.ENCODING)));
                    }
                }

                return conn.get();
            }
        } catch (ConnectException | SocketTimeoutException | UnknownHostException e) {
            log.error(ERROR_MSG_TMPL, url, e);
            throw new NovelConnectException();
        } catch (HttpStatusException e) {
            log.error(ERROR_MSG_TMPL, url, e);
            if (e.getStatusCode() == 404) {
                throw new NovelNotFoundException();
            }
            throw new NovelConnectException();
        } catch (IOException e) {
            log.error(ERROR_MSG_TMPL, url, e);
            throw new NovelNotFoundException();
        }
    }

    /**
     * 指定されたミリ秒数の間、実行を停止する.
     */
    public static void delayAccess() {
        try {
            Thread.sleep(DELAY_ACCESS_TIME);
        } catch (InterruptedException e) {
            log.warn("Interrupted:", e);
            Thread.currentThread().interrupt();
        }
    }
}
