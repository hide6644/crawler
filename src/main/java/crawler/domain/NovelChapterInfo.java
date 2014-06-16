package crawler.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 小説の章の付随情報
 */
@Entity
@Table(name = "novel_chapter_info")
public class NovelChapterInfo extends BaseEntity implements Serializable {

    /** 最終確認日時 */
    private Date checkedDate;

    /** 最終更新日時 */
    private Date modifiedDate;

    /** 未読フラグ */
    private boolean unread;

    /** 既読日時 */
    private Date readDate;

    /** 小説の章 */
    private NovelChapter novelChapter;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "checked_date")
    public Date getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(Date checkedDate) {
        this.checkedDate = checkedDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date")
    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Column
    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "read_date")
    public Date getReadDate() {
        return readDate;
    }

    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "novel_chapter_id")
    public NovelChapter getNovelChapter() {
        return novelChapter;
    }

    public void setNovelChapter(NovelChapter novelChapter) {
        this.novelChapter = novelChapter;
    }

    /*
     * (非 Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    public final int hashCode() {
        return hashCode(getId());
    }

    /*
     * (非 Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof NovelChapterInfo) {
            return equals(getId(), ((NovelChapterInfo) obj).getId());
        } else {
            return false;
        }
    }
}
