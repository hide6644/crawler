package crawler.entity;

import java.io.Serializable;

/**
 * 権限
 */
public enum Role implements Serializable {

    /** 管理者 */
    ROLE_ADMIN,

    /** 一般 */
    ROLE_USER;
}
