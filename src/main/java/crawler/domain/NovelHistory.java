package crawler.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 小説の更新履歴
 */
@Entity
@Table(name = "novel_history")
public class NovelHistory extends BaseEntity implements Serializable {

    /** タイトル */
    private String title;

    /** 作者名 */
    private String writername;

    /** 解説 */
    private String description;

    /** 本文 */
    private String body;

    /** 小説 */
    private Novel novel;

    @Column(length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 100)
    public String getWritername() {
        return writername;
    }

    public void setWritername(String writername) {
        this.writername = writername;
    }

    @Column
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id")
    public Novel getNovel() {
        return novel;
    }

    public void setNovel(Novel novel) {
        this.novel = novel;
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
        } else if (obj instanceof NovelHistory) {
            return equals(getId(), ((NovelHistory) obj).getId());
        } else {
            return false;
        }
    }
}
