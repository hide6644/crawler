package crawler.mapping.yomou.syosetu.com;

import java.util.Optional;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
        return !element.getElementsByTag("a").isEmpty();
    }

    /**
     * 小説のhtml sourceから小説のタイトルを取得する.
     *
     * @param html
     *            html source
     * @return 小説のタイトル
     */
    public static String getTitle(final Document html) {
        return html.getElementsByClass("novel_title").first().text();
    }

    /**
     * 小説のhtml sourceから小説の作者を取得する.
     *
     * @param html
     *            html source
     * @return 小説の作者
     */
    public static String getWritername(final Document html) {
        return html.getElementsByClass("novel_writername").first().text().replace("作者：", "");
    }

    /**
     * 小説のhtml sourceから小説の解説を取得する.
     *
     * @param html
     *            html source
     * @return 小説の解説
     */
    public static String getDescription(final Document html) {
        return html.getElementById("novel_ex").text();
    }

    /**
     * 小説のhtml sourceから小説の本文を取得する.
     *
     * @param html
     *            html source
     * @return 小説の本文
     */
    public static String getBody(final Document html) {
        return html.getElementsByClass("index_box").first().html();
    }

    /**
     * 小説のhtml sourceから小説の付随情報のURLを取得する.
     *
     * @param html
     *            html source
     * @return 小説の付随情報のURL
     */
    public static String getNovelInfoUrl(final Document html) {
        return html.getElementById("novel_header").getElementsByTag("a").stream()
                .filter(linkElement -> linkElement.text().equals("小説情報"))
                .map(linkElement -> linkElement.attr("href"))
                .findFirst().orElse(null);
    }

    /**
     * 小説の本文のhtml elementから小説の章のURLを取得する.
     *
     * @param element
     *            html element
     * @return 小説の章のURL
     */
    public static String getChapterUrlByNovelBody(final Element element) {
        return element.getElementsByTag("a").first().attr("href");
    }

    /**
     * 小説の章のhtml sourceから小説の章のタイトルを取得する.
     *
     * @param html
     *            html source
     * @return 小説の章のタイトル
     */
    public static String getChapterTitle(final Document html) {
        return html.getElementsByClass("novel_subtitle").first().text();
    }

    /**
     * 小説の本文のhtml elementから小説の章のタイトルを取得する.
     *
     * @param element
     *            html element
     * @return 小説の章のタイトル
     */
    public static String getChapterTitleByNovelBody(final Element element) {
        return Optional.ofNullable(element.getElementsByClass("period_subtitle").first())
                .orElse(Optional.ofNullable(element.getElementsByClass("long_subtitle").first())
                        .orElse(element.getElementsByClass("subtitle").first()))
                .getElementsByTag("a").html();
    }

    /**
     * 小説の章のhtml sourceから小説の章の本文を取得する.
     *
     * @param html
     *            html source
     * @return 小説の章の本文
     */
    public static String getChapterBody(final Document html) {
        return html.getElementById("novel_honbun").html();
    }

    /**
     * 小説の付随情報のhtml sourceから小説の付随情報のキーワードを取得する.
     *
     * @param html
     *            html source
     * @return 小説の付随情報のキーワード
     */
    public static String getKeyword(final Document html) {
        return html.getElementById("noveltable1").getElementsByTag("td").get(2).text();
    }

    /**
     * 小説の付随情報のhtml sourceから小説の付随情報の最終更新日時を取得する.
     *
     * @param html
     *            html source
     * @return 小説の付随情報の最終更新日時
     */
    public static String getModifiedDate(final Document html) {
        return html.getElementById("noveltable2").getElementsByTag("td").get(1).text();
    }

    /**
     * 小説の付随情報のhtml sourceから小説の付随情報の完結フラグを取得する.
     *
     * @param html
     *            html source
     * @return 小説の付随情報の完結フラグ
     */
    public static boolean getFinished(final Document html) {
        Element finishedElement = html.getElementById("noveltype");

        return finishedElement != null && finishedElement.text().equals("完結済");
    }

    /**
     * 小説の目次のhtml elementから小説の章の最終更新日時を取得する.
     *
     * @param element
     *            html element
     * @return 小説の章の最終更新日時
     */
    public static String getChapterModifiedDate(final Element element) {
        Element updateElement = element.getElementsByClass("long_update").first();

        if (updateElement.getElementsByTag("span").isEmpty()) {
            return updateElement.text();
        } else {
            return updateElement.getElementsByTag("span").first().attr("title");
        }
    }
}
