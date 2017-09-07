package crawler.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.search.query.facet.Facet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import crawler.dao.jpa.GenericDaoJpa;
import crawler.domain.Novel;
import crawler.domain.NovelInfo;

public class NovelInfoDaoTest extends BaseDaoTestCase {

    @Autowired
    private NovelInfoDao dao;

    @Before
    public void setUp() {
        GenericDao<Novel, Long> genericDao = new GenericDaoJpa<Novel, Long>(Novel.class, entityManager);

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

        genericDao.save(novel);
    }

    @Test
    public void testSearch() throws Exception {
        dao.reindex();
        List<NovelInfo> novelInfoList = dao.search("Keyword1");

        assertNotNull(novelInfoList);

        novelInfoList = dao.search(new String[]{"Keyword1"}, new String[]{"keyword"});

        assertNotNull(novelInfoList);
    }

    @Test
    public void testFacet() throws Exception {
        dao.reindex();
        List<Facet> facet = dao.facet("keywordSet.keyword", 2);

        assertNotNull(facet);
    }
}
