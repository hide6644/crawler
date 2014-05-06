package crawler.dao.hibernate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import crawler.dao.NovelDao;
import crawler.domain.Novel;

/**
 * 小説DAOの実装クラス.
 */
@Repository("novelDao")
public class NovelDaoHibernate extends GenericDaoHibernate<Novel, Long> implements NovelDao {

    /**
     * デフォルト・コンストラクタ.
     */
    public NovelDaoHibernate() {
        super(Novel.class);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.NovelDao#getNovelsByCheckedDate(java.util.Date)
     */
    @Override
    public List<Novel> getNovelsByCheckedDate(Date checkedDate) {
        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("checkedDate", checkedDate);

        return findByNamedQuery("crawler.dao.NovelDao.getNovelsByCheckedDate", queryParams);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.NovelDao#getNovelsByUnread()
     */
    @Override
    public List<Novel> getNovelsByUnread() {
        return findByNamedQuery("crawler.dao.NovelDao.getNovelsByUnread", null);
    }
}
