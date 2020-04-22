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

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Normalizer;
import org.hibernate.search.annotations.NormalizerDef;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.TokenFilterDef;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 小説の章の情報
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "novel_chapter")
@Indexed
@Analyzer(impl = JapaneseAnalyzer.class)
@NormalizerDef(name = "novelChapterSort", filters = @TokenFilterDef(factory = LowerCaseFilterFactory.class))
public class NovelChapter extends BaseObject implements Serializable {

    /** URL */
    @Column(nullable = false, length = 64)
    private String url;

    /** タイトル */
    @EqualsAndHashCode.Exclude
    @Column(length = 100)
    @Field
    @Field(name = "titleSort", normalizer = @Normalizer(definition = "novelChapterSort"))
    @SortableField(forField = "titleSort")
    private String title;

    /** 本文 */
    @EqualsAndHashCode.Exclude
    @Column
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Field
    private String body;

    /** 小説の章の付随情報 */
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "novelChapter", cascade = CascadeType.ALL)
    private NovelChapterInfo novelChapterInfo;

    /** 小説の章の更新履歴セット */
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "novelChapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NovelChapterHistory> novelChapterHistories = new HashSet<>();

    /** 小説 */
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id")
    @ContainedIn
    private Novel novel;

    public void addNovelChapterHistory(NovelChapterHistory novelChapterHistory) {
        novelChapterHistories.add(novelChapterHistory);
        novelChapterHistory.setNovelChapter(this);
    }

    public void removeNovelChapterHistory(NovelChapterHistory novelChapterHistory) {
        novelChapterHistories.remove(novelChapterHistory);
        novelChapterHistory.setNovelChapter(null);
    }
}
