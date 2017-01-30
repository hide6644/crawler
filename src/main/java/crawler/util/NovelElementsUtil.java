package crawler.util;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

/**
 * 小説のhtml要素抽出のUtilityクラス.
 */
public class NovelElementsUtil {

    /**
     * プライベート・コンストラクタ.
     * Utilityクラスはインスタンス化禁止.
     */
    private NovelElementsUtil() {
    }

    /**
     * html要素の種類
     */
    public enum ContensType {
        PERIOD_SUBTITLE, LONG_SUBTITLE, SUBTITLE
    };

    /**
     * 小説の章へのリンクが存在するか.
     *
     * @param element
     *            html element要素
     * @return true:リンクが存在する、false:リンクが存在しない
     */
    public static boolean existsChapterLink(final Element element) {
        return element.getAllElements("a").size() > 0;
    }

    /**
     * 小説の章が存在するか.
     *
     * @param html
     *            html要素
     * @return true:章が存在する、false:章が存在しない
     */
    public static boolean existsChapter(final Source html) {
        return html.getAllElementsByClass("novel_subtitle").size() > 0;
    }

    /**
     * 小説のhtml要素から小説のタイトルを取得する.
     *
     * @param html
     *            html要素
     * @return 小説のタイトル
     */
    public static String getTitle(final Source html) {
        return html.getAllElementsByClass("novel_title").get(0).getTextExtractor().toString();
    }

    /**
     * 小説のhtml要素から小説の作者を取得する.
     *
     * @param html
     *            html要素
     * @return 小説の作者
     */
    public static String getWritername(final Source html) {
        return html.getAllElementsByClass("novel_writername").get(0).getTextExtractor().toString().replaceAll("作者：", "");
    }

    /**
     * 小説のhtml要素から小説の解説を取得する.
     *
     * @param html
     *            html要素
     * @return 小説の解説
     */
    public static String getDescription(final Source html) {
        return html.getElementById("novel_ex").getTextExtractor().toString();
    }

    /**
     * 小説のhtml要素から小説の本文を取得する.
     *
     * @param html
     *            html要素
     * @return 小説の本文
     */
    public static String getBody(final Source html) {
        return html.getAllElementsByClass("index_box").get(0).toString();
    }

    /**
     * 小説の本文のhtml element要素から小説の章のURLを取得する.
     *
     * @param element
     *            html element要素
     * @return 小説の章のURL
     */
    public static String getChapterUrlByNovelBody(final Element element) {
        return element.getAllElements("a").get(0).getAttributeValue("href");
    }

    /**
     * 小説の章のhtml要素から小説の章のタイトルを取得する.
     *
     * @param html
     *            html要素
     * @return 小説の章のタイトル
     */
    public static String getChapterTitle(final Source html) {
        return html.getAllElementsByClass("novel_subtitle").get(0).getTextExtractor().toString();
    }

    /**
     * 小説の本文のhtml element要素から小説の章のタイトルを取得する.
     *
     * @param element
     *            html element要素
     * @param type
     *            html要素の種類
     * @return 小説の章のタイトル
     */
    public static String getChapterTitleByNovelBody(final Element element, final ContensType type) {
        switch (type) {
        case PERIOD_SUBTITLE:
            return element.getAllElementsByClass("period_subtitle").get(0).getAllElements("a").get(0).toString();
        case LONG_SUBTITLE:
            return element.getAllElementsByClass("long_subtitle").get(0).getAllElements("a").get(0).toString();
        case SUBTITLE:
            return element.getAllElementsByClass("subtitle").get(0).getAllElements("a").get(0).toString();
        default:
            throw new IllegalArgumentException();
        }
    }

    /**
     * 小説の章のhtml要素から小説の章の本文を取得する.
     *
     * @param html
     *            html要素
     * @return 小説の章の本文
     */
    public static String getChapterBody(final Source html) {
        return html.getElementById("novel_honbun").toString();
    }

    /**
     * 小説の付随情報のhtml要素から小説の付随情報のキーワードを取得する.
     *
     * @param html
     *            html要素
     * @return 小説の付随情報のキーワード
     */
    public static String getKeyword(final Source html) {
        return html.getElementById("noveltable1").getAllElements("td").get(2).getTextExtractor().toString();
    }

    /**
     * 小説の付随情報のhtml要素から小説の付随情報の最終更新日時を取得する.
     *
     * @param html
     *            html要素
     * @return 小説の付随情報の最終更新日時
     */
    public static String getModifiedDate(final Source html) {
        return html.getElementById("noveltable2").getAllElements("td").get(1).getTextExtractor().toString();
    }

    /**
     * 小説の付随情報のhtml要素から小説の付随情報の完結フラグを取得する.
     *
     * @param html
     *            html要素
     * @return 小説の付随情報の完結フラグ
     */
    public static boolean getFinished(final Source html) {
        Element finishedElement = html.getElementById("noveltype");

        if (finishedElement != null && finishedElement.getTextExtractor().toString().equals("完結済")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 小説の本文のhtml element要素から小説の章の最終更新日時を取得する.
     *
     * @param element
     *            html element要素
     * @param extract
     *            true:括弧内を参照する、false:括弧内を参照しない
     * @return 小説の章の最終更新日時
     */
    public static String getChapterModifiedDate(final Element element, final boolean extract) {
        Element updateElement = element.getAllElementsByClass("long_update").get(0);

        if (extract && updateElement.getAllElements("span").size() > 0) {
            return updateElement.getAllElements("span").get(0).getAttributeValue("title").toString();
        } else {
            return updateElement.getTextExtractor().toString();
        }
    }
}
