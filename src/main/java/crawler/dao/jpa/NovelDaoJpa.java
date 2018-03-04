package crawler.dao.jpa;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import crawler.dao.NovelDao;
import crawler.domain.Novel;

/**
 * 小説DAOの実装クラス.
 */
@Repository("novelDao")
public class NovelDaoJpa extends GenericDaoJpa<Novel, Long> implements NovelDao {

    /**
     * デフォルト・コンストラクタ.
     */
    public NovelDaoJpa() {
        super(Novel.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Novel getByUrl(String url) {
        try {
            return entityManager.createNamedQuery("Novel.findByUrl", persistentClass).setParameter("url", url).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Novel> getByCheckedDateLessThanEqualAndCheckEnableTrue(LocalDateTime checkedDate) {
        return entityManager.createNamedQuery("Novel.findByDeletedFalseAndCheckedDateLessThanEqualAndCheckEnableTrue", persistentClass).setParameter("checkedDate", checkedDate).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Novel> getByUnreadTrueOrderByTitleAndNovelChapterId() {
        return entityManager.createNamedQuery("Novel.findByUnreadTrueOrderByTitleAndNovelChapterId", persistentClass).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Novel> getOrderByTitle() {
        return entityManager.createNamedQuery("Novel.findByDeletedFalseOrderByTitle", persistentClass).getResultList();
    }
}
