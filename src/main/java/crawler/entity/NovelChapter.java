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
public class NovelChapter extends BaseObject implements Serializable {

    /** URL */
    @Column(nullable = false, length = 64)
    private String url;

    /** タイトル */
    @EqualsAndHashCode.Exclude
    @Column(length = 100)
    private String title;

    /** 本文 */
    @EqualsAndHashCode.Exclude
    @Column
    @Lob
    @Basic(fetch = FetchType.LAZY)
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
    private Novel novel;

    /**
     * 小説の章の更新履歴を追加する.
     *
     * @param novelChapterHistory 小説の章の更新履歴
     */
    public void addNovelChapterHistory(NovelChapterHistory novelChapterHistory) {
        novelChapterHistories.add(novelChapterHistory);
        novelChapterHistory.setNovelChapter(this);
    }

    /**
     * 小説の章の更新履歴を追加する.
     *
     * @param novelChapterHistory 小説の章の更新履歴
     */
    public void removeNovelChapterHistory(NovelChapterHistory novelChapterHistory) {
        novelChapterHistories.remove(novelChapterHistory);
        novelChapterHistory.setNovelChapter(null);
    }
}
