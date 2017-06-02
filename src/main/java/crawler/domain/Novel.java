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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * 小説の情報
 */
@Entity
@Table(name = "novel")
@Indexed
@Analyzer(impl = JapaneseAnalyzer.class)
@XmlRootElement
public class Novel extends BaseObject implements Serializable {

    /** ログ出力クラス */
    protected Logger log = LogManager.getLogger(getClass());

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

    /** 小説の更新履歴セット */
    private Set<NovelHistory> novelHistories = new HashSet<NovelHistory>();

    /** 小説の章リスト */
    private List<NovelChapter> novelChapters = new ArrayList<NovelChapter>();

    /**
     * 更新を確認する必要があるか.
     * (更新頻度から判定する)
     *
     * @return true:確認必要、false:確認不要
     */
    public boolean needsCheckForUpdate() {
        final DateTime now = DateTime.now();
        if (novelInfo.isFinished()) {
            if (new DateTime(novelInfo.getCheckedDate()).isAfter(now.minusDays(45))) {
                log.info("[skip] finished title:" + title);
                return false;
            }
        }

        final DateTime modifiedDate = new DateTime(novelInfo.getModifiedDate());
        if (modifiedDate.isAfter(now.minusDays(30))) {
            // 更新日付が30日以内の場合
            if (new DateTime(novelInfo.getCheckedDate()).isAfter(now.minusDays((int) new Duration(modifiedDate, now).getStandardDays() / 2))) {
                log.info("[skip] title:" + title);
                return false;
            }
        } else {
            if (new DateTime(novelInfo.getCheckedDate()).isAfter(now.minusDays(15))) {
                log.info("[skip] title:" + title);
                return false;
            }
        }

        return true;
    }

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
