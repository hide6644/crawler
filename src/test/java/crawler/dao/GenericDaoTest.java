package crawler.dao;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;

import crawler.dao.jpa.GenericDaoJpa;
import crawler.domain.Novel;

public class GenericDaoTest extends BaseDaoTestCase {

    GenericDao<Novel, Long> genericDao;

    public static final String PERSISTENCE_UNIT_NAME = "ApplicationEntityManager";

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    protected EntityManager entityManager;

    @Before
    public void setUp() {
        genericDao = new GenericDaoJpa<Novel, Long>(Novel.class, entityManager);
    }

    @Test
    public void testGet() {
        Novel novel = genericDao.get(1L);
        assertNotNull(novel);
        assertTrue(genericDao.exists(novel.getId()));
    }

    @Test
    public void testGetAll() {
        List<Novel> novelList = genericDao.getAll();
        assertTrue(novelList.size() > 1);
    }

    @Test
    public void testGetAllDistinct() {
        List<Novel> novelDistinctList = genericDao.getAllDistinct();
        assertTrue(novelDistinctList.size() > 1);
    }
}
