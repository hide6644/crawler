package crawler.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import crawler.domain.Novel;
import crawler.domain.NovelHistory;
import crawler.util.NovelElementsUtil.ContensType;
import net.htmlparser.jericho.Element;
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
     *            小説のURL
     * @return URLオブジェクト
     */
    public static URL getUrl(final String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            log.error("[error] url:" + url, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 小説のhtml要素を取得する.
     *
     * @param url
     *            URLオブジェクト
     * @return 小説のhtml要素
     */
    public static Source getSource(final URL url) {
        // ネットワーク負荷低減のため、1秒間隔で取得
        delayAccess();

        try {
            Source html = new Source(url);
            html.fullSequentialParse();
            return html;
        } catch (FileNotFoundException e) {
            log.error("[error] url:" + url, e);
            return null;
        } catch (IOException e) {
            log.error("[error] url:" + url, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 1秒間実行を停止する.
     */
    public static void delayAccess() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
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

    /**
     * 小説の章の情報が更新されているか確認する.
     *
     * @param element
     *            html element要素
     * @param novelHistoryBody
     *            小説の更新履歴の本文
     * @return true:更新有り、false:更新無し
     */
    public static boolean hasUpdatedChapter(final Element element, final NovelHistory novelHistory) {
        if (novelHistory == null) {
            // Historyが無い場合、trueを返却
            return true;
        }

        String subtitle = NovelElementsUtil.getChapterTitleByNovelBody(element, NovelElementsUtil.ContensType.SUBTITLE);
        String chapterModifiedDate = NovelElementsUtil.getChapterModifiedDate(element, false);
        Source novelHistoryBodyHtml = new Source(novelHistory.getBody());

        // 過去のコンテンツ
        for (Element chapterHistory : novelHistoryBodyHtml.getAllElements("tr")) {
            if (chapterHistory.getAllElementsByClass("period_subtitle").size() > 0) {
                if (isDifferentSubtitle(subtitle, chapterModifiedDate, chapterHistory, NovelElementsUtil.ContensType.PERIOD_SUBTITLE)) {
                    // 更新なし
                    return false;
                }
            } else if (chapterHistory.getAllElementsByClass("long_subtitle").size() > 0) {
                if (isDifferentSubtitle(subtitle, chapterModifiedDate, chapterHistory, NovelElementsUtil.ContensType.LONG_SUBTITLE)) {
                    // 更新なし
                    return false;
                }
            }
        }

        // 現在のコンテンツ
        for (Element chapterHistory : novelHistoryBodyHtml.getAllElements("dl")) {
            if (isDifferentSubtitle(subtitle, chapterModifiedDate, chapterHistory, NovelElementsUtil.ContensType.SUBTITLE)) {
                // 更新なし
                return false;
            }
        }

        // 更新あり
        return true;
    }

    /**
     * 小説の章の情報に差異があるか.
     *
     * @param subtitle
     *            小説の章のタイトル
     * @param chapterModifiedDate
     *            小説の章の更新日付
     * @param chapterHistory
     *            保存済みの小説の章のhtml element要素
     * @param type
     *            html要素の種類
     * @return true:小説の章のHTML要素が一致、false:小説の章のHTML要素が不一致
     */
    public static boolean isDifferentSubtitle(String subtitle, String chapterModifiedDate, Element chapterHistory, ContensType type) {
        String subtitleHistory = NovelElementsUtil.getChapterTitleByNovelBody(chapterHistory, type);
        String chapterModifiedDateHistory = NovelElementsUtil.getChapterModifiedDate(chapterHistory, false);

        if (subtitle.equals(subtitleHistory) && chapterModifiedDate.equals(chapterModifiedDateHistory)) {
            // 小説の章のHTML要素が一致する場合
            return true;
        } else {
            return false;
        }
    }
}