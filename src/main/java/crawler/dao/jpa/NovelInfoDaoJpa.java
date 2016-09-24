package crawler.dao.jpa;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import crawler.dao.NovelInfoDao;
import crawler.domain.NovelInfo;

/**
 * 小説付随情報のDAOの実装クラス.
 */
@Repository("novelInfoDao")
public class NovelInfoDaoJpa extends GenericDaoJpa<NovelInfo, Long> implements NovelInfoDao {

    /**
     * デフォルト・コンストラクタ.
     */
    public NovelInfoDaoJpa() {
        super(NovelInfo.class);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.NovelInfoDao#searchNovelInfoByKeyword(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<NovelInfo> searchNovelInfoByKeyword(String keyword) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(NovelInfo.class).get();
        Query jpaQuery = fullTextEntityManager.createFullTextQuery(queryBuilder.keyword().onField("keyword").matching(keyword).createQuery(), NovelInfo.class);

        return jpaQuery.getResultList();
    }
}
