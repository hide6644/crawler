package crawler.domain.source;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import crawler.util.NovelElementsUtil;
import net.htmlparser.jericho.Element;

/**
 * 小説の目次のhtml elementを保持するクラス.
 */
public class NovelIndexElement {

    /** 小説の目次に記載されている章のURL */
    private String chapterUrl;

    /** 小説の目次に記載されている章のリンク */
    private String chapterLink;

    /** 小説の目次に記載されている章の最終更新日時 */
    private String chapterModifiedDate;

    /**
     * コンストラクタ.
     *
     * @param element
     *            小説の目次のhtml element
     */
    public NovelIndexElement(Element element) {
        chapterUrl = NovelElementsUtil.getChapterUrlByNovelBody(element);
        chapterLink = NovelElementsUtil.getChapterTitleByNovelBody(element);
        chapterModifiedDate = NovelElementsUtil.getChapterModifiedDate(element);
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
        return new HashCodeBuilder()
                .append(chapterLink)
                .append(chapterModifiedDate).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof NovelIndexElement)) {
            return false;
        }

        NovelIndexElement castObj = (NovelIndexElement) obj;
        return new EqualsBuilder()
                .append(chapterLink, castObj.chapterLink)
                .append(chapterModifiedDate, castObj.chapterModifiedDate)
                .isEquals();
    }
}
