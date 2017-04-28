package crawler.domain.source;

import crawler.util.NovelElementsUtil;
import net.htmlparser.jericho.Element;

/**
 * 小説の本文の目次のhtml
 */
public class NovelBodyIndexElement {

    /** 小説の本文の目次のhtml */
    private Element element;

    /** 小説の本文に記載されている章のURL */
    private String chapterUrl;

    /** 小説の本文に記載されている章のリンク */
    private String chapterLink;

    /** 小説の本文に記載されている章の最終更新日時 */
    private String chapterModifiedDate;

    /**
     * コンストラクタ.
     *
     * @param element
     *            小説の本文のリンクのhtml
     */
    public NovelBodyIndexElement(Element element) {
        this(element, NovelElementsUtil.getChapterUrlByNovelBody(element),
                NovelElementsUtil.getChapterTitleByNovelBody(element),
                NovelElementsUtil.getChapterModifiedDate(element, false));
    }

    /**
     * コンストラクタ.
     *
     * @param element
     *            小説の本文の目次のhtml
     * @param chapterUrl
     *            小説の本文に記載されている章のURL
     * @param chapterLink
     *            小説の本文に記載されている章のリンク
     * @param chapterModifiedDate
     *            小説の本文に記載されている章の最終更新日時
     */
    public NovelBodyIndexElement(Element element, String chapterUrl, String chapterLink, String chapterModifiedDate) {
        this.element = element;
        this.chapterUrl = chapterUrl;
        this.chapterLink = chapterLink;
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

    public String getChapterLink() {
        return chapterLink;
    }

    public void setChapterLink(String chapterLink) {
        this.chapterLink = chapterLink;
    }

    public String getChapterModifiedDate() {
        return chapterModifiedDate;
    }

    public void setChapterModifiedDate(String chapterModifiedDate) {
        this.chapterModifiedDate = chapterModifiedDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((chapterLink == null) ? 0 : chapterLink.hashCode());
        result = prime * result + ((chapterModifiedDate == null) ? 0 : chapterModifiedDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NovelBodyIndexElement other = (NovelBodyIndexElement) obj;
        if (chapterLink == null) {
            if (other.chapterLink != null) {
                return false;
            }
        } else if (!chapterLink.equals(other.chapterLink)) {
            return false;
        }
        if (chapterModifiedDate == null) {
            if (other.chapterModifiedDate != null) {
                return false;
            }
        } else if (!chapterModifiedDate.equals(other.chapterModifiedDate)) {
            return false;
        }
        return true;
    }
}
