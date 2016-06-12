package crawler.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import crawler.domain.Novel;
import crawler.service.impl.NovelElementsUtil.ContensType;
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
    static URL getUrl(final String url) {
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
    static Source getSource(final URL url) {
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
    static void delayAccess() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    /**
     * 小説を確認するか、更新頻度から判断する.
     *
     * @param novel
     *            小説の情報
     * @return true:確認必要、false:確認不要
     */
    static boolean isConfirmedNovel(final Novel novel) {
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
     * @param htmlHistory
     *            小説の更新履歴の本文
     * @return true:更新有り、false:更新無し
     */
    static boolean hasUpdatedChapter(final Element element, final Source htmlHistory) {
        String subtitle = NovelElementsUtil.getChapterTitleByNovelBody(element, NovelElementsUtil.ContensType.SUBTITLE);
        String chapterUpdateDate = NovelElementsUtil.getChapterModifiedDate(element, false);

        // 過去のコンテンツ
        for (Element chapterHistory : htmlHistory.getAllElements("tr")) {
            if (chapterHistory.getAllElementsByClass("period_subtitle").size() > 0) {
                if (isDifferentSubtitle(subtitle, chapterUpdateDate, chapterHistory, NovelElementsUtil.ContensType.PERIOD_SUBTITLE)) {
                    // 変更なし
                    return false;
                }
            } else if (chapterHistory.getAllElementsByClass("long_subtitle").size() > 0) {
                if (isDifferentSubtitle(subtitle, chapterUpdateDate, chapterHistory, NovelElementsUtil.ContensType.LONG_SUBTITLE)) {
                    // 変更なし
                    return false;
                }
            }
        }

        // 現在のコンテンツ
        for (Element chapterHistory : htmlHistory.getAllElements("dl")) {
            if (isDifferentSubtitle(subtitle, chapterUpdateDate, chapterHistory, NovelElementsUtil.ContensType.SUBTITLE)) {
                // 変更なし
                return false;
            }
        }

        return true;
    }

    /**
     * 小説の章の情報に差異があるか.
     * 
     * @param subtitle
     *            小説の章のタイトル
     * @param chapterUpdateDate
     *            小説の章の更新日付
     * @param chapterHistory
     *            保存済みの小説の章のhtml element要素
     * @param type
     *            html要素の種類
     * @return true:小説の章のHTML要素が一致、false:小説の章のHTML要素が不一致
     */
    static boolean isDifferentSubtitle(String subtitle, String chapterUpdateDate, Element chapterHistory, ContensType type) {
        String subtitleHistory = NovelElementsUtil.getChapterTitleByNovelBody(chapterHistory, type);
        String chapterUpdateDateHistory = NovelElementsUtil.getChapterModifiedDate(chapterHistory, false);

        if (subtitle.equals(subtitleHistory) && chapterUpdateDate.equals(chapterUpdateDateHistory)) {
            // 小説の章のHTML要素が一致する場合
            return true;
        } else {
            return false;
        }
    }
}
