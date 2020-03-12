package crawler.mapping;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jsoup.nodes.Element;

import crawler.util.NovelElementsUtil;

/**
 * 小説の目次のhtml elementを保持するクラス.
 */
public class NovelIndexElement {

    /** 小説の目次に記載されている章のURL */
    private final String chapterUrl;

    /** 小説の目次に記載されている章のリンク */
    private final String chapterLink;

    /** 小説の目次に記載されている章の最終更新日時 */
    private final String chapterModifiedDate;

    /**
     * コンストラクタ.
     *
     * @param element
     *            小説の目次のhtml element
     */
    public NovelIndexElement(final Element element) {
        chapterUrl = NovelElementsUtil.getChapterUrlByNovelBody(element);
        chapterLink = NovelElementsUtil.getChapterTitleByNovelBody(element);
        chapterModifiedDate = NovelElementsUtil.getChapterModifiedDate(element);
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public String getChapterLink() {
        return chapterLink;
    }

    public String getChapterModifiedDate() {
        return chapterModifiedDate;
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
