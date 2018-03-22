package crawler.util;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

/**
 * 小説のhtml source抽出のUtilityクラス.
 */
public class NovelElementsUtil {

    /**
     * プライベート・コンストラクタ.
     * Utilityクラスはインスタンス化禁止.
     */
    private NovelElementsUtil() {
    }

    /**
     * 小説の章へのリンクが存在するか.
     *
     * @param element
     *            html element
     * @return true:リンクが存在する、false:リンクが存在しない
     */
    public static boolean existsChapterLink(final Element element) {
        return !element.getAllElements("a").isEmpty();
    }

    /**
     * 小説のhtml sourceから小説のタイトルを取得する.
     *
     * @param html
     *            html source
     * @return 小説のタイトル
     */
    public static String getTitle(final Source html) {
        return html.getAllElementsByClass("novel_title").get(0).getTextExtractor().toString();
    }

    /**
     * 小説のhtml sourceから小説の作者を取得する.
     *
     * @param html
     *            html source
     * @return 小説の作者
     */
    public static String getWritername(final Source html) {
        return html.getAllElementsByClass("novel_writername").get(0).getTextExtractor().toString().replaceAll("作者：", "");
    }

    /**
     * 小説のhtml sourceから小説の解説を取得する.
     *
     * @param html
     *            html source
     * @return 小説の解説
     */
    public static String getDescription(final Source html) {
        return html.getElementById("novel_ex").getTextExtractor().toString();
    }

    /**
     * 小説のhtml sourceから小説の本文を取得する.
     *
     * @param html
     *            html source
     * @return 小説の本文
     */
    public static String getBody(final Source html) {
        return html.getAllElementsByClass("index_box").get(0).toString();
    }

    /**
     * 小説の本文のhtml elementから小説の章のURLを取得する.
     *
     * @param element
     *            html element
     * @return 小説の章のURL
     */
    public static String getChapterUrlByNovelBody(final Element element) {
        return element.getAllElements("a").get(0).getAttributeValue("href");
    }

    /**
     * 小説の章のhtml sourceから小説の章のタイトルを取得する.
     *
     * @param html
     *            html source
     * @return 小説の章のタイトル
     */
    public static String getChapterTitle(final Source html) {
        return html.getAllElementsByClass("novel_subtitle").get(0).getTextExtractor().toString();
    }

    /**
     * 小説の本文のhtml elementから小説の章のタイトルを取得する.
     *
     * @param element
     *            html element
     * @return 小説の章のタイトル
     */
    public static String getChapterTitleByNovelBody(final Element element) {
        if (!element.getAllElementsByClass("period_subtitle").isEmpty()) {
            return element.getAllElementsByClass("period_subtitle").get(0).getAllElements("a").get(0).toString();
        } else if (!element.getAllElementsByClass("long_subtitle").isEmpty()) {
            return element.getAllElementsByClass("long_subtitle").get(0).getAllElements("a").get(0).toString();
        } else {
            return element.getAllElementsByClass("subtitle").get(0).getAllElements("a").get(0).toString();
        }
    }

    /**
     * 小説の章のhtml sourceから小説の章の本文を取得する.
     *
     * @param html
     *            html source
     * @return 小説の章の本文
     */
    public static String getChapterBody(final Source html) {
        return html.getElementById("novel_honbun").toString();
    }

    /**
     * 小説の付随情報のhtml sourceから小説の付随情報のキーワードを取得する.
     *
     * @param html
     *            html source
     * @return 小説の付随情報のキーワード
     */
    public static String getKeyword(final Source html) {
        return html.getElementById("noveltable1").getAllElements("td").get(2).getTextExtractor().toString();
    }

    /**
     * 小説の付随情報のhtml sourceから小説の付随情報の最終更新日時を取得する.
     *
     * @param html
     *            html source
     * @return 小説の付随情報の最終更新日時
     */
    public static String getModifiedDate(final Source html) {
        return html.getElementById("noveltable2").getAllElements("td").get(1).getTextExtractor().toString();
    }

    /**
     * 小説の付随情報のhtml sourceから小説の付随情報の完結フラグを取得する.
     *
     * @param html
     *            html source
     * @return 小説の付随情報の完結フラグ
     */
    public static boolean getFinished(final Source html) {
        Element finishedElement = html.getElementById("noveltype");

        return finishedElement != null && finishedElement.getTextExtractor().toString().equals("完結済");
    }

    /**
     * 小説の目次のhtml elementから小説の章の最終更新日時を取得する.
     *
     * @param element
     *            html element
     * @return 小説の章の最終更新日時
     */
    public static String getChapterModifiedDate(final Element element) {
        Element updateElement = element.getAllElementsByClass("long_update").get(0);

        if (updateElement.getAllElements("span").isEmpty()) {
            return updateElement.getTextExtractor().toString();
        } else {
            return updateElement.getAllElements("span").get(0).getAttributeValue("title");
        }
    }
}
