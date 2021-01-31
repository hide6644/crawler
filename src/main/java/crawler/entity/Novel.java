package crawler.entity;

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

import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 小説の情報
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "novel")
@Indexed
@XmlRootElement
public class Novel extends BaseObject implements Serializable {

    /** URL */
    @Column(nullable = false, length = 64)
    private String url;

    /** タイトル */
    @EqualsAndHashCode.Exclude
    @Column(length = 100)
    @FullTextField(analyzer = "japanese")
    @KeywordField(name = "titleSort", sortable = Sortable.YES)
    private String title;

    /** 作者名 */
    @EqualsAndHashCode.Exclude
    @Column(length = 100)
    @FullTextField(analyzer = "japanese")
    @KeywordField(name = "writernameSort", sortable = Sortable.YES)
    private String writername;

    /** 解説 */
    @EqualsAndHashCode.Exclude
    @Column
    @FullTextField(analyzer = "japanese")
    @KeywordField(name = "descriptionSort", sortable = Sortable.YES)
    private String description;

    /** 本文 */
    @EqualsAndHashCode.Exclude
    @Column
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @FullTextField(analyzer = "japanese")
    private String body;

    /** 削除フラグ */
    @EqualsAndHashCode.Exclude
    @Column
    private boolean deleted;

    /** 小説の付随情報 */
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "novel", cascade = CascadeType.ALL)
    @IndexedEmbedded
    private NovelInfo novelInfo;

    /** 小説の更新履歴セット */
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "novel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NovelHistory> novelHistories = new HashSet<>();

    /** 小説の章リスト */
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "novel", cascade = CascadeType.ALL, orphanRemoval = true)
    @IndexedEmbedded
    private List<NovelChapter> novelChapters = new ArrayList<>();

    /**
     * 小説の更新履歴を追加する.
     *
     * @param novelHistory 小説の更新履歴
     */
    public void addNovelHistory(NovelHistory novelHistory) {
        novelHistories.add(novelHistory);
        novelHistory.setNovel(this);
    }

    /**
     * 小説の更新履歴を削除する.
     *
     * @param novelHistory 小説の更新履歴
     */
    public void removeNovelHistory(NovelHistory novelHistory) {
        novelHistories.remove(novelHistory);
        novelHistory.setNovel(null);
    }

    /**
     * 小説の章を追加する.
     *
     * @param novelChapter 小説の章
     */
    public void addNovelChapter(NovelChapter novelChapter) {
        novelChapters.add(novelChapter);
        novelChapter.setNovel(this);
    }

    /**
     * 小説の章を削除する.
     *
     * @param novelChapter 小説の章
     */
    public void removeNovelChapter(NovelChapter novelChapter) {
        novelChapters.remove(novelChapter);
        novelChapter.setNovel(null);
    }
}
