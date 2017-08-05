package crawler.service;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.query.facet.Facet;
import org.junit.Before;
import org.junit.Test;

import crawler.dao.jpa.GenericDaoJpa;
import crawler.domain.Novel;
import crawler.service.impl.GenericManagerImpl;

public class GenericManagerTest extends BaseManagerTestCase {

    public static final String PERSISTENCE_UNIT_NAME = "ApplicationEntityManager";

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    protected EntityManager entityManager;

    GenericManager<Novel, Long> genericManager;

    @Before
    public void setUp() {
        genericManager = new GenericManagerImpl<Novel, Long>(new GenericDaoJpa<Novel, Long>(Novel.class, entityManager));
    }

    @Test
    public void testSearch() throws Exception {
        genericManager.reindexAll(false);
        List<Novel> novelList = genericManager.search("class");

        assertNotNull(novelList);
        assertEquals(1, novelList.size());
    }

    @Test
    public void testFacet() throws Exception {
        genericManager.reindexAll(false);
        List<Facet> novelFacet = genericManager.facet("keywordSet.keyword", 2);

        assertNotNull(novelFacet);
        assertEquals(2, novelFacet.size());
    }
}
