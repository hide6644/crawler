package crawler.service.impl;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import crawler.domain.Novel;

/**
 * 小説の情報のUtilityクラス.
 */
public class NovelManagerUtil {

    /** ログ出力クラス */
    private static final Log log = LogFactory.getLog(NovelManagerUtil.class);

    /**
     * プライベート・コンストラクタ.<br />
     * Utilityクラスはインスタンス化禁止.
     */
    private NovelManagerUtil() {
    }

    /**
     * 小説の更新を確認するか、更新頻度から判断する.
     *
     * @param novel
     *            小説の情報
     * @return true:小説の更新を確認する
     */
    static boolean checkUpdateFrequency(Novel novel) {
        if (novel.getNovelInfo().isFinished()) {
            if (new DateTime(novel.getNovelInfo().getCheckedDate()).isAfter(new DateTime().minusDays(30))) {
                log.info("[skip] finished title:" + novel.getTitle());
                return false;
            }
        }

        if (new DateTime(novel.getNovelInfo().getModifiedDate()).isBefore(new DateTime().minusDays(30))) {
            if (new DateTime(novel.getNovelInfo().getCheckedDate()).isAfter(new DateTime().minusDays(15))) {
                log.info("[skip] title:" + novel.getTitle());
                return false;
            }
        }

        if (new DateTime(novel.getNovelInfo().getModifiedDate()).isBefore(new DateTime().minusDays(14))) {
            if (new DateTime(novel.getNovelInfo().getCheckedDate()).isAfter(new DateTime().minusDays(7))) {
                log.info("[skip] title:" + novel.getTitle());
                return false;
            }
        }

        if (new DateTime(novel.getNovelInfo().getModifiedDate()).isBefore(new DateTime().minusDays(3))) {
            if (new DateTime(novel.getNovelInfo().getCheckedDate()).isAfter(new DateTime().minusDays(3))) {
                log.info("[skip] title:" + novel.getTitle());
                return false;
            }
        }

        return true;
    }

    /**
     * 小説の章の情報が更新されているか確認する.
     *
     * @param subtitle
     *            小説の章のタイトル
     * @param chapterUpdateDate
     *            小説の章の更新日
     * @param html
     *            html要素
     * @return true:更新あり
     */
    static boolean checkChapterUpdate(String subtitle, String chapterUpdateDate, Source html) {
        for (Element chapterHistory : html.getAllElements("tr")) {
            if (chapterHistory.getAllElementsByClass("period_subtitle").size() > 0) {
                String subtitleHistory = chapterHistory.getAllElementsByClass("period_subtitle").get(0).getAllElements("a").get(0).toString();
                String chapterUpdateDateHistory = chapterHistory.getAllElementsByClass("long_update").get(0).getTextExtractor().toString();

                if (subtitle.equals(subtitleHistory) && chapterUpdateDate.equals(chapterUpdateDateHistory)) {
                    // 小説の章のHTML要素が一致する場合、変更なし
                    return false;
                }
            } else if (chapterHistory.getAllElementsByClass("long_subtitle").size() > 0) {
                String subtitleHistory = chapterHistory.getAllElementsByClass("long_subtitle").get(0).getAllElements("a").get(0).toString();
                String chapterUpdateDateHistory = chapterHistory.getAllElementsByClass("long_update").get(0).getTextExtractor().toString();

                if (subtitle.equals(subtitleHistory) && chapterUpdateDate.equals(chapterUpdateDateHistory)) {
                    // 小説の章のHTML要素が一致する場合、変更なし
                    return false;
                }
            }
        }

        for (Element chapterHistory : html.getAllElements("dl")) {
            String subtitleHistory = chapterHistory.getAllElementsByClass("subtitle").get(0).getAllElements("a").get(0).toString();
            String chapterUpdateDateHistory = chapterHistory.getAllElementsByClass("long_update").get(0).getTextExtractor().toString();

            if (subtitle.equals(subtitleHistory) && chapterUpdateDate.equals(chapterUpdateDateHistory)) {
                // 小説の章のHTML要素が一致する場合、変更なし
                return false;
            }
        }

        return true;
    }
}
