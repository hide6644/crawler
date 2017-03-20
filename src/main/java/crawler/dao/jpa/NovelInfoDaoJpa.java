package crawler.dao.jpa;

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
}
