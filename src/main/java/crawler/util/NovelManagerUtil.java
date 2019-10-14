package crawler.util;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
            return new URL(url);
        } catch (MalformedURLException e) {
            log.error("url:{}", url, e);
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
                return Jsoup.connect(url).get();
            }
        } catch (ConnectException | SocketTimeoutException e) {
            log.error("url:{}", url, e);
            throw new NovelConnectException();
        } catch (HttpStatusException e) {
            log.error("url:{}", url, e);
            if (e.getStatusCode() == 404) {
                throw new NovelNotFoundException();
            }
            throw new NovelConnectException();
        } catch (IOException e) {
            log.error("url:{}", url, e);
            throw new NovelNotFoundException();
        }
    }

    /**
     * 指定されたミリ秒数の間、実行を停止する.
     */
    public static void delayAccess() {
        try {
            Thread.sleep(Constants.DELAY_ACCESS_TIME);
        } catch (InterruptedException e) {
            log.warn("Interrupted:", e);
            Thread.currentThread().interrupt();
        }
    }
}
