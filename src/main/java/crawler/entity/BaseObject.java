package crawler.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * エンティティの基底クラス.
 */
@MappedSuperclass
public abstract class BaseObject implements Serializable {

    /** ID */
    private Long id;

    /** 更新回数 */
    private Long version;

    /** 登録ユーザ */
    private String createUser;

    /** 登録日時 */
    private LocalDateTime createDate;

    /** 更新ユーザ */
    private String updateUser;

    /** 更新日時 */
    private LocalDateTime updateDate;

    /**
     * IDを取得する.
     *
     * @return ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlTransient
    public Long getId() {
        return id;
    }

    /**
     * IDを設定する.
     *
     * @param id
     *            ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 更新回数を取得する.
     *
     * @return 更新回数
     */
    @Version
    @XmlTransient
    public Long getVersion() {
        return version;
    }

    /**
     * 更新回数を設定する.
     *
     * @param version
     *            更新回数
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * 登録ユーザを取得する.
     *
     * @return 登録ユーザ
     */
    @Column(name = "create_user")
    @XmlTransient
    public String getCreateUser() {
        return createUser;
    }

    /**
     * 登録ユーザを設定する.
     *
     * @param createUser
     *            登録ユーザ
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * 登録日時を取得する.
     *
     * @return 更新日時
     */
    @Column(name = "create_date", updatable = false)
    @XmlTransient
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * 登録日時を設定する.
     *
     * @param createDate
     *            登録日時
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * 更新ユーザを取得する.
     *
     * @return 更新ユーザ
     */
    @Column(name = "update_user")
    @XmlTransient
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * 更新ユーザを設定する.
     *
     * @param updateUser
     *            更新ユーザ
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * 更新日時を取得する.
     *
     * @return 更新日時
     */
    @Column(name = "update_date")
    @XmlTransient
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /**
     * 更新日時を設定する.
     *
     * @param updateDate
     *            更新日時
     */
    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }

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
                .append(id, castObj.id)
                .isEquals();
    }
}
