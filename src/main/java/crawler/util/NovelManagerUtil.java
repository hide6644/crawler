package crawler.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import crawler.Constants;
import crawler.domain.Novel;
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
        // ネットワーク負荷低減のため、一時的に実行を停止
        delayAccess();

        try {
            Source html = new Source(url);
            html.fullSequentialParse();
            return html;
        } catch (FileNotFoundException e) {
            log.error("url:" + url, e);
            throw new NovelNotFoundException();
        } catch (IOException e) {
            log.error("url:" + url, e);
            throw new NovelNotFoundException();
        }
    }

    /**
     * 指定されたミリ秒数の間、一時的に実行を停止する.
     */
    public static void delayAccess() {
        try {
            Thread.sleep(Constants.DELAY_ACCESS_TIME);
        } catch (InterruptedException e) {
            log.error("Interrupted:", e);
        }
    }

    /**
     * 小説の更新を確認するか、更新頻度から判断する.
     *
     * @param novel
     *            小説の情報
     * @return true:確認必要、false:確認不要
     */
    public static boolean isConfirmedNovel(final Novel novel) {
        if (novel.getNovelInfo().isFinished()) {
            if (new DateTime(novel.getNovelInfo().getCheckedDate()).isAfter(DateTime.now().minusDays(45))) {
                log.info("[skip] finished title:" + novel.getTitle());
                return false;
            }
        }

        DateTime modifiedDate = new DateTime(novel.getNovelInfo().getModifiedDate());
        if (modifiedDate.isAfter(DateTime.now().minusDays(30))) {
            // 更新日付が30日以内の場合
            if (new DateTime(novel.getNovelInfo().getCheckedDate())
                    .isAfter(DateTime.now().minusDays((int) new Duration(modifiedDate, DateTime.now()).getStandardDays() / 2))) {
                log.info("[skip] title:" + novel.getTitle());
                return false;
            }
        } else {
            if (new DateTime(novel.getNovelInfo().getCheckedDate()).isAfter(DateTime.now().minusDays(15))) {
                log.info("[skip] title:" + novel.getTitle());
                return false;
            }
        }

        return true;
    }
}
