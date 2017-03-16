package crawler.dao.jpa;

import javax.persistence.NoResultException;

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
     * @see crawler.dao.NovelChapterDao#getByUrl(java.lang.String)
     */
    @Override
    public NovelChapter getByUrl(String url) {
        try {
            return entityManager.createNamedQuery("NovelChapter.findByUrl", persistentClass).setParameter("url", url).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
