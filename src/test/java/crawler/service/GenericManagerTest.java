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
import crawler.domain.NovelInfo;
import crawler.service.impl.GenericManagerImpl;

public class GenericManagerTest extends BaseManagerTestCase {

    public static final String PERSISTENCE_UNIT_NAME = "ApplicationEntityManager";

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    protected EntityManager entityManager;

    GenericManager<Novel, Long> genericManager;

    @Before
    public void setUp() {
        genericManager = new GenericManagerImpl<Novel, Long>(new GenericDaoJpa<Novel, Long>(Novel.class, entityManager));

        Novel novel = new Novel();
        novel.setUrl("Url");
        novel.setTitle("Title");
        novel.setWritername("Writername");
        novel.setDescription("Description");
        novel.setBody("Body");

        NovelInfo novelInfo = new NovelInfo();
        novelInfo.setKeyword("Keyword1 Keyword2");
        novelInfo.setNovel(novel);
        novel.setNovelInfo(novelInfo);

        genericManager.save(novel);
    }

    @Test
    public void testGet() {
        List<Novel> novelList = genericManager.getAll();

        assertNotNull(genericManager.get(novelList.get(0).getId()));
    }

    @Test
    public void testExists() {
        List<Novel> novelList = genericManager.getAll();

        assertTrue(genericManager.exists(novelList.get(0).getId()));
        assertFalse(genericManager.exists(-1L));
    }

    @Test
    public void testSearch() {
        genericManager.reindexAll(false);
        List<Novel> novelList = genericManager.search("Body");

        assertNotNull(novelList);
        assertTrue(novelList.size() > 0);
    }

    @Test
    public void testFacet() {
        genericManager.reindexAll(false);
        List<Facet> novelFacet = genericManager.facet("keywordSet.keyword", 2);

        assertNotNull(novelFacet);
        assertEquals(2, novelFacet.size());
    }
}
