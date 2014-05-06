package crawler.dao.hibernate;

import java.util.List;

import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.stereotype.Repository;

import crawler.dao.NovelInfoDao;
import crawler.domain.NovelInfo;

/**
 * 小説付随情報のDAOの実装クラス.
 */
@Repository("novelInfoDao")
public class NovelInfoDaoHibernate extends GenericDaoHibernate<NovelInfo, Long> implements NovelInfoDao {

    /**
     * デフォルト・コンストラクタ.
     */
    public NovelInfoDaoHibernate() {
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
        FullTextSession txtSession = Search.getFullTextSession(getSession());
        return txtSession.createFullTextQuery(
                txtSession.getSearchFactory().buildQueryBuilder().forEntity(NovelInfo.class).get().keyword()
                        .onFields("keyword").matching(keyword).createQuery(), NovelInfo.class).list();
    }
}
