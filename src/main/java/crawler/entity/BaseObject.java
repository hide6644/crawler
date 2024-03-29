package crawler.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import jakarta.xml.bind.annotation.XmlTransient;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * エンティティの基底クラス.
 */
@Setter
@Getter
@EqualsAndHashCode
@MappedSuperclass
public abstract class BaseObject implements Serializable {

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @DocumentId
    @XmlTransient
    private Long id;

    /** 更新回数 */
    @EqualsAndHashCode.Exclude
    @Version
    @XmlTransient
    private Long version;

    /** 登録ユーザ */
    @EqualsAndHashCode.Exclude
    @Column(name = "create_user")
    @XmlTransient
    private String createUser;

    /** 登録日時 */
    @EqualsAndHashCode.Exclude
    @Column(name = "create_date", updatable = false)
    @XmlTransient
    private LocalDateTime createDate;

    /** 更新ユーザ */
    @EqualsAndHashCode.Exclude
    @Column(name = "update_user")
    @XmlTransient
    private String updateUser;

    /** 更新日時 */
    @EqualsAndHashCode.Exclude
    @Column(name = "update_date")
    @XmlTransient
    private LocalDateTime updateDate;
}
