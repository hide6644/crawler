package crawler.dao.jpa;

import org.springframework.stereotype.Repository;

import crawler.entity.Novel;

/**
 * NovelのHibernate Search DAOの実装クラス.
 */
@Repository("novelSearch")
public class NovelSearchImpl extends HibernateSearchImpl<Novel> {

    /**
     * デフォルト・コンストラクタ.
     */
    public NovelSearchImpl() {
        super(Novel.class);
    }
}
