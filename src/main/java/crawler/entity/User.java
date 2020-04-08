package crawler.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー.
 */
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode()
@ToString
@Entity
@Table(name = "app_user")
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class User implements Serializable {

    /** ユーザー名 */
    @NonNull
    @Id
    @Column(nullable = false, length = 16, unique = true)
    private String username;

    /** パスワード */
    @EqualsAndHashCode.Exclude
    @Column(nullable = false, length = 80)
    private String password;

    /** ｅメール */
    @EqualsAndHashCode.Exclude
    @Column(nullable = false, length = 64, unique = true)
    private String email;

    /** 有効 */
    @EqualsAndHashCode.Exclude
    @Column(nullable = false)
    private Boolean enabled;

    /** 権限 */
    @EqualsAndHashCode.Exclude
    @Column(nullable = false)
    @CollectionTable(name = "app_user_roles", joinColumns = @JoinColumn(name = "username"))
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    private List<Role> roles;

    /** ユーザーの小説の付随情報 */
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserNovelInfo> userNovelInfos = new LinkedHashSet<>();

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

    public void addUserNovelInfo(UserNovelInfo userNovelInfo) {
        userNovelInfos.add(userNovelInfo);
    }
}
