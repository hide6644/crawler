package crawler.domain.source;

import crawler.util.NovelElementsUtil;
import net.htmlparser.jericho.Element;

/**
 * 小説の本文のhtml
 */
public class NovelBodyElement {

    /** 小説の本文のhtml */
    private Element element;

    /** 小説の本文に記載されている章のURL */
    private String chapterUrl;

    /** 小説の本文に記載されている章のタイトル */
    private String chapterTitle;

    /** 小説の本文に記載されている章の最終更新日時 */
    private String chapterModifiedDate;

    /**
     * コンストラクタ.
     *
     * @param element
     *            小説の本文のhtml
     */
    public NovelBodyElement(Element element) {
        this(element, NovelElementsUtil.getChapterUrlByNovelBody(element),
                NovelElementsUtil.getChapterTitleByNovelBody(element),
                NovelElementsUtil.getChapterModifiedDate(element, false));
    }

    /**
     * コンストラクタ.
     *
     * @param element
     *            小説の本文のhtml
     * @param chapterUrl
     *            小説の本文に記載されている章のURL
     * @param chapterTitle
     *            小説の本文に記載されている章のタイトル
     * @param chapterModifiedDate
     *            小説の本文に記載されている章の最終更新日時
     */
    public NovelBodyElement(Element element, String chapterUrl, String chapterTitle, String chapterModifiedDate) {
        this.element = element;
        this.chapterUrl = chapterUrl;
        this.chapterTitle = chapterTitle;
        this.chapterModifiedDate = chapterModifiedDate;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public String getChapterModifiedDate() {
        return chapterModifiedDate;
    }

    public void setChapterModifiedDate(String chapterModifiedDate) {
        this.chapterModifiedDate = chapterModifiedDate;
    }
}
