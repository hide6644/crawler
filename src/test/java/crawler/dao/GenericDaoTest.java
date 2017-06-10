package crawler.dao;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
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

        Novel novel = new Novel();
        novel.setUrl("Url");
        novel.setTitle("Title");
        novel.setWritername("Writername");
        novel.setDescription("Description");
        novel.setBody("Body");
        genericDao.save(novel);
    }

    @Test
    public void testGet() {
        Map<String, Object> param = new HashMap<>();
        param.put("url", "Url");
        List<Novel> novelList = genericDao.findByNamedQuery("Novel.findByUrl", param);
        assertNotNull(novelList);
        assertTrue(genericDao.exists(novelList.get(0).getId()));

        Novel novel = genericDao.get(novelList.get(0).getId());
        assertNotNull(novel);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetInvalid() throws Exception {
        genericDao.get(-1L);
    }

    @Test
    public void testGetAll() {
        List<Novel> novelList = genericDao.getAll();
        assertTrue(novelList.size() > 0);
    }

    @Test
    public void testGetAllDistinct() {
        List<Novel> novelDistinctList = genericDao.getAllDistinct();
        assertTrue(novelDistinctList.size() > 0);
    }
}
