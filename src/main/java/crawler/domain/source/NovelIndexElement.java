package crawler.domain.source;

import java.util.Date;

import org.joda.time.format.DateTimeFormat;

import crawler.domain.NovelChapterInfo;
import crawler.util.NovelElementsUtil;
import net.htmlparser.jericho.Element;

/**
 * 小説の目次のhtml elementを保持するクラス.
 */
public class NovelIndexElement {

    /** 最終更新日時のフォーマット */
    public static final String MODIFIED_DATE_FORMAT = "yyyy/MM/dd HH:mm";

    /** 小説の目次に記載されている章のURL */
    private String chapterUrl;

    /** 小説の目次に記載されている章のリンク */
    private String chapterLink;

    /** 小説の目次に記載されている章の最終更新日時 */
    private String chapterModifiedDate;

    /** 小説の章の付随情報 */
    private NovelChapterInfo novelChapterInfo;

    /**
     * コンストラクタ.
     *
     * @param element
     *            小説の目次のhtml element要素
     */
    public NovelIndexElement(Element element) {
        chapterUrl = NovelElementsUtil.getChapterUrlByNovelBody(element);
        chapterLink = NovelElementsUtil.getChapterTitleByNovelBody(element);
        chapterModifiedDate = NovelElementsUtil.getChapterModifiedDate(element);
    }

    /**
     * 小説の目次のhtml element要素を小説の章の付随情報(NovelChapterInfo)に変換する.
     */
    public void mapping() {
        if (novelChapterInfo == null) {
            novelChapterInfo = new NovelChapterInfo();
        } else {
            // 更新の場合
            novelChapterInfo.setUpdateDate(new Date());
        }

        novelChapterInfo.setCheckedDate(new Date());
        novelChapterInfo.setModifiedDate(DateTimeFormat.forPattern(MODIFIED_DATE_FORMAT)
                .parseDateTime(chapterModifiedDate.replaceAll(" 改稿", "")).toDate());
        novelChapterInfo.setUnread(true);
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

    public NovelChapterInfo getNovelChapterInfo() {
        return novelChapterInfo;
    }

    public void setNovelChapterInfo(NovelChapterInfo novelChapterInfo) {
        this.novelChapterInfo = novelChapterInfo;
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
        NovelIndexElement other = (NovelIndexElement) obj;
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
