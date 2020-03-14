package crawler.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 小説の章の付随情報
 */
@Setter
@Getter
@Entity
@Table(name = "novel_chapter_info")
public class NovelChapterInfo extends BaseObject implements Serializable {

    /** 最終確認日時 */
    @Column(name = "checked_date")
    private LocalDateTime checkedDate;

    /** 最終更新日時 */
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    /** 未読フラグ */
    @Column
    private boolean unread;

    /** 既読日時 */
    @Column(name = "read_date")
    private LocalDateTime readDate;

    /** 小説の章 */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "novel_chapter_id")
    private NovelChapter novelChapter;
}
