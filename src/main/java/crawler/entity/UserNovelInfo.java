package crawler.entity;

import java.io.Serializable;

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
 * ユーザーの小説の付随情報
 */
@Setter
@Getter
@Entity
@Table(name = "user_novel_info")
public class UserNovelInfo extends BaseObject implements Serializable {

    /** お気に入りフラグ */
    @Column
    private boolean favorite;

    /** 評価 */
    @Column
    private Integer rank;

    /** ユーザー */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private User user;

    /** 小説 */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "novel_id")
    private Novel novel;
}
