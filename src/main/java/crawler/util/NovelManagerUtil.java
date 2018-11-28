package crawler.util;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import crawler.Constants;
import crawler.exception.NovelConnectException;
import crawler.exception.NovelNotFoundException;
import net.htmlparser.jericho.Source;

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
            log.error("url:" + url, e);
            throw new NovelNotFoundException();
        }
    }

    /**
     * html sourceを取得する.
     *
     * @param url
     *            URLオブジェクト
     * @return html source
     * @throws NovelNotFoundException
     *             URLで指定されたコンテンツが見つからない
     */
    public static Source getSource(final URL url) throws NovelNotFoundException {
        // ネットワーク負荷低減のため、実行を一時停止
        delayAccess();

        try {
            Source html = new Source(url);
            html.fullSequentialParse();
            return html;
        } catch (ConnectException e) {
            log.error("url:" + url, e);
            throw new NovelConnectException();
        } catch (IOException e) {
            log.error("url:" + url, e);
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
