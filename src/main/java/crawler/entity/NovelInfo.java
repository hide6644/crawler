package crawler.entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

/**
 * 小説の付随情報
 */
@Setter
@Getter
@Entity
@Table(name = "novel_info")
public class NovelInfo extends BaseObject implements Serializable {

    /** ログ出力クラス */
    private static final Logger log = LogManager.getLogger(NovelInfo.class);

    /** 最終確認日時 */
    @Column(name = "checked_date")
    private LocalDateTime checkedDate;

    /** 最終更新日時 */
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    /** 完結フラグ */
    @Column
    private boolean finished;

    /** キーワード */
    @Column(length = 300)
    private String keyword;

    /** お気に入りフラグ */
    @Column
    private boolean favorite;

    /** 評価 */
    @Column
    private Integer rank;

    /** 更新確認有効 */
    @Column(name = "check_enable")
    private boolean checkEnable;

    /** 小説 */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "novel_id")
    private Novel novel;

    /**
     * 更新を確認する必要があるか.
     * (更新頻度から判定する)
     *
     * @return true:確認必要、false:確認不要
     */
    public boolean needsCheckForUpdate() {
        final LocalDateTime now = LocalDateTime.now();
        if (finished && checkedDate.isAfter(now.minusDays(45))) {
            // 完了済み、かつ確認日が45日以内の場合
            log.info("[skip] finished title:{}", () -> novel.getTitle());
            return false;
        }

        if (modifiedDate.isAfter(now.minusDays(30))) {
            // 更新日付が30日以内の場合
            if (checkedDate.isAfter(now.minusDays(Duration.between(modifiedDate, now).dividedBy(2).toDays()))) {
                // 確認日時が更新日の半分の期間より後の場合
                log.info("[skip] title:{}", () -> novel.getTitle());
                return false;
            }
        } else if (checkedDate.isAfter(now.minusDays(15))) {
            // 確認日時が15日以内の場合
            log.info("[skip] title:{}", () -> novel.getTitle());
            return false;
        }

        return true;
    }
}
