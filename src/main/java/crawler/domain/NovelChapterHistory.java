package crawler.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 小説の章の更新履歴
 */
@Entity
@Table(name = "novel_chapter_history")
public class NovelChapterHistory extends BaseEntity implements Serializable {

    /** タイトル */
    private String title;

    /** 本文 */
    private String body;

    /** 小説の章 */
    private NovelChapter novelChapter;

    @Column(length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @ManyToOne(fetch = FetchType.LAZY)
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
        } else if (obj instanceof NovelChapterHistory) {
            return equals(getId(), ((NovelChapterHistory) obj).getId());
        } else {
            return false;
        }
    }
}
