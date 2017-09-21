package crawler.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 * 小説の章の情報
 */
@Entity
@Table(name = "novel_chapter")
@Indexed
@Analyzer(impl = JapaneseAnalyzer.class)
public class NovelChapter extends BaseObject implements Serializable {

    /** URL */
    private String url;

    /** タイトル */
    private String title;

    /** 本文 */
    private String body;

    /** 小説の章の付随情報 */
    private NovelChapterInfo novelChapterInfo;

    /** 小説の章の更新履歴セット */
    private Set<NovelChapterHistory> novelChapterHistories = new HashSet<>();

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
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Field
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
        NovelChapter other = (NovelChapter) obj;
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
