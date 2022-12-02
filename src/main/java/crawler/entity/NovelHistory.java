package crawler.entity;

import java.io.Serializable;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;

import lombok.Getter;
import lombok.Setter;

/**
 * 小説の更新履歴
 */
@Setter
@Getter
@Entity
@Table(name = "novel_history")
public class NovelHistory extends BaseObject implements Serializable {

    /** タイトル */
    @Column(length = 100)
    private String title;

    /** 作者名 */
    @Column(length = 100)
    private String writername;

    /** 解説 */
    @Column
    private String description;

    /** 本文 */
    @Column
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String body;

    /** 小説 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id")
    private Novel novel;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BaseObject castObj = (BaseObject) obj;
        return new EqualsBuilder()
                .append(getId(), castObj.getId())
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 31;
    }
}
