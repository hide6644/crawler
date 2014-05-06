package crawler.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * 小説
 */
@Entity
@Table(name = "novel")
@Indexed
@Analyzer(impl = JapaneseAnalyzer.class)
@XmlRootElement
public class Novel extends BaseEntity implements Serializable {

    /** URL */
    private String url;

    /** タイトル */
    private String title;

    /** 作者名 */
    private String writername;

    /** 解説 */
    private String description;

    /** 本文 */
    private String body;

    /** 最終確認日時 */
    private Date checkedDate;

    /** 最終更新日時 */
    private Date modifiedDate;

    /** 完結フラグ */
    private boolean finished;

    /** 削除フラグ */
    private boolean deleted;

    /** 小説の付随情報 */
    private NovelInfo novelInfo;

    /** 小説の更新履歴 */
    private Set<NovelHistory> novelHistories = new HashSet<NovelHistory>();

    /** 小説の章 */
    private List<NovelChapter> novelChapters = new ArrayList<NovelChapter>();

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

    @Column(length = 100)
    @Field
    public String getWritername() {
        return writername;
    }

    public void setWritername(String writername) {
        this.writername = writername;
    }

    @Column
    @Field
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Column
    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Column
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "novel", cascade = CascadeType.ALL)
    @IndexedEmbedded
    public NovelInfo getNovelInfo() {
        return novelInfo;
    }

    public void setNovelInfo(NovelInfo novelInfo) {
        this.novelInfo = novelInfo;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "novel", cascade = CascadeType.ALL)
    public Set<NovelHistory> getNovelHistories() {
        return novelHistories;
    }

    public void setNovelHistories(Set<NovelHistory> novelHistories) {
        this.novelHistories = novelHistories;
    }

    public void addNovelHistory(NovelHistory novelHistory) {
        getNovelHistories().add(novelHistory);
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "novel", cascade = CascadeType.ALL)
    @IndexedEmbedded
    public List<NovelChapter> getNovelChapters() {
        return novelChapters;
    }

    public void setNovelChapters(List<NovelChapter> novelChapters) {
        this.novelChapters = novelChapters;
    }

    public void addNovelChapter(NovelChapter novel) {
        getNovelChapters().add(novel);
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
        } else if (obj instanceof Novel) {
            return equals(url, ((Novel) obj).getUrl());
        } else {
            return false;
        }
    }
}
