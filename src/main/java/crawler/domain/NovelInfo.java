package crawler.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Facet;
import org.hibernate.search.annotations.FacetEncodingType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * 小説の付随情報
 */
@Entity
@Table(name = "novel_info")
@Indexed
public class NovelInfo extends BaseObject implements Serializable {

    /** 最終確認日時 */
    private Date checkedDate;

    /** 最終更新日時 */
    private Date modifiedDate;

    /** 完結フラグ */
    private boolean finished;

    /** キーワード */
    private String keyword;

    /** キーワードセット */
    private Set<KeywordWrap> keywordSet = new HashSet<>();

    /** お気に入りフラグ */
    private boolean favorite;

    /** 評価 */
    private Integer rank;

    /** 小説 */
    private Novel novel;

    /**
     * 更新を確認する必要があるか.
     * (更新頻度から判定する)
     *
     * @return true:確認必要、false:確認不要
     */
    public boolean needsCheckForUpdate() {
        final Logger log = LogManager.getLogger(NovelInfo.class);

        final DateTime now = DateTime.now();
        final DateTime checkedDateTime = new DateTime(checkedDate);
        if (finished && checkedDateTime.isAfter(now.minusDays(45))) {
            // 完了済み、かつ確認日が45日以内の場合
            log.info("[skip] finished title:" + novel.getTitle());
            return false;
        }

        final DateTime modifiedDateTime = new DateTime(modifiedDate);
        if (modifiedDateTime.isAfter(now.minusDays(30))) {
            // 更新日付が30日以内の場合
            if (checkedDateTime.isAfter(now.minusDays((int) new Duration(modifiedDateTime, now).getStandardDays() / 2))) {
                // 確認日時が更新日の半分の期間より後の場合
                log.info("[skip] title:" + novel.getTitle());
                return false;
            }
        } else if (checkedDateTime.isAfter(now.minusDays(15))) {
            // 確認日時が15日以内の場合
            log.info("[skip] title:" + novel.getTitle());
            return false;
        }

        return true;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "checked_date")
    public Date getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(Date checkedDate) {
        this.checkedDate = checkedDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date")
    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Column
    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Column(length = 300)
    @Field
    @Analyzer(impl = WhitespaceAnalyzer.class)
    public String getKeyword() {
        return keyword;
    }

    /**
     * キーワードを設定する.
     * スペースで分割したキーワードをKeywordWrapに設定する.
     *
     * @param keyword
     *            キーワード
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;

        Stream.of(Optional.ofNullable(keyword).orElseGet(String::new).split(" "))
                .collect(Collectors.toSet()).forEach(keywords -> keywordSet.add(new KeywordWrap(keywords)));
    }

    @Transient
    @IndexedEmbedded
    @OneToMany
    public Set<KeywordWrap> getKeywordSet() {
        return keywordSet;
    }

    @Column
    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Column
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "novel_id")
    @ContainedIn
    public Novel getNovel() {
        return novel;
    }

    public void setNovel(Novel novel) {
        this.novel = novel;
    }
}

/**
 * 小説の付随情報のキーワード
 */
class KeywordWrap implements Serializable {

    /** キーワード */
    @Field(analyze = Analyze.NO)
    @Facet(encoding = FacetEncodingType.STRING)
    String keyword;

    /**
     * コンストラクタ.
     *
     * @param keyword
     *            小説の付随情報のキーワード
     */
    public KeywordWrap(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}