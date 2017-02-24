package crawler.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import crawler.dao.NovelChapterDao;
import crawler.domain.NovelChapter;

/**
 * 小説DAOの実装クラス.
 */
@Repository("novelChapterDao")
public class NovelChapterDaoJpa extends GenericDaoJpa<NovelChapter, Long> implements NovelChapterDao {

    /**
     * デフォルト・コンストラクタ.
     */
    public NovelChapterDaoJpa() {
        super(NovelChapter.class);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.NovelChapterDao#getNovelChaptersByUrl(java.lang.String)
     */
    @Override
    public NovelChapter getNovelChaptersByUrl(String url) {
        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("url", url);

        List<NovelChapter> novelChapterList = findByNamedQuery(NovelChapter.FIND_BY_URL, queryParams);
        if (novelChapterList.isEmpty()) {
            return null;
        } else {
            return novelChapterList.get(0);
        }
    }
}
