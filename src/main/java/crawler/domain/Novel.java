package crawler.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * 小説の情報
 */
@Entity
@Table(name = "novel")
@NamedQueries({
    @NamedQuery(
        name = Novel.FIND_BY_CHECKED_DATE,
        query = "from Novel n join fetch n.novelInfo ni where n.deleted = false and ni.checkedDate <= :checkedDate"
    ),
    @NamedQuery(
        name = Novel.FIND_BY_UNREAD,
        query = "select distinct n from Novel n join fetch n.novelInfo ni join fetch n.novelChapters nc join fetch nc.novelChapterInfo nci where nci.unread = true order by n.title, nc.id"
    )
})
@Indexed
@Analyzer(impl = JapaneseAnalyzer.class)
@XmlRootElement
public class Novel extends BaseEntity implements Serializable {

    /** 最終確認日時で検索するクエリ */
    public static final String FIND_BY_CHECKED_DATE = "Novel.getNovelsByCheckedDate";

    /** 未読フラグで検索するクエリ */
    public static final String FIND_BY_UNREAD = "Novel.getNovelsByUnread";

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
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Field
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
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
        Novel other = (Novel) obj;
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        return true;
    }
}
