package crawler.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 小説の章の更新履歴
 */
@Setter
@Getter
@Entity
@Table(name = "novel_chapter_history")
public class NovelChapterHistory extends BaseObject implements Serializable {

    /** タイトル */
    @Column(length = 100)
    private String title;

    /** 本文 */
    @Column
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String body;

    /** 小説の章 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_chapter_id")
    private NovelChapter novelChapter;
}
