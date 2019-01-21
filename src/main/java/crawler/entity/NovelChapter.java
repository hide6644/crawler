package crawler.entity;

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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 小説の章の情報
 */
@Entity
@Table(name = "novel_chapter")
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column
    @Lob
    @Basic(fetch = FetchType.LAZY)
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
    public Novel getNovel() {
        return novel;
    }

    public void setNovel(Novel novel) {
        this.novel = novel;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(url).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof NovelChapter)) {
            return false;
        }

        NovelChapter castObj = (NovelChapter) obj;
        return new EqualsBuilder()
                .append(url, castObj.url)
                .isEquals();
    }
}
