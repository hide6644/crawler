package crawler.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 * 小説の章
 */
@Entity
@Table(name = "novel_chapter")
@Indexed
@Analyzer(impl = JapaneseAnalyzer.class)
public class NovelChapter extends BaseEntity implements Serializable {

    /** URL */
    private String url;

    /** タイトル */
    private String title;

    /** 本文 */
    private String body;

    /** 最終確認日時 */
    private Date checkedDate;

    /** 最終更新日時 */
    private Date modifiedDate;

    /** 小説の章の付随情報 */
    private NovelChapterInfo novelChapterInfo;

    /** 小説の章の更新履歴 */
    private Set<NovelChapterHistory> novelChapterHistories = new HashSet<NovelChapterHistory>();

    /** 小説 */
    private Novel novel;

    @Column(nullable = false, length = 64)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(length = 100)
    @Field
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column
    @Field
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

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

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "novelChapter", cascade = CascadeType.ALL)
    public NovelChapterInfo getNovelChapterInfo() {
        return novelChapterInfo;
    }

    public void setNovelChapterInfo(NovelChapterInfo novelChapterInfo) {
        this.novelChapterInfo = novelChapterInfo;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "novelChapter", cascade = CascadeType.ALL)
    public Set<NovelChapterHistory> getNovelChapterHistories() {
        return novelChapterHistories;
    }

    public void setNovelChapterHistories(Set<NovelChapterHistory> novelChapterHistories) {
        this.novelChapterHistories = novelChapterHistories;
    }

    public void addNovelChapterHistory(NovelChapterHistory novelChapterHistory) {
        getNovelChapterHistories().add(novelChapterHistory);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id")
    @ContainedIn
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
        return hashCode(url);
    }

    /*
     * (非 Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof NovelChapter) {
            return equals(url, ((NovelChapter) obj).getUrl());
        } else {
            return false;
        }
    }
}
