package crawler.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.search.query.facet.Facet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import crawler.dao.jpa.HibernateSearchImpl;
import crawler.domain.Novel;
import crawler.domain.NovelInfo;

public class HibernateSearchTest extends BaseDaoTestCase {

    @Autowired
    private NovelDao dao;

    HibernateSearch<NovelInfo> hibernateSearch;

    @Before
    public void setUp() {
        hibernateSearch = new HibernateSearchImpl<NovelInfo>(NovelInfo.class, entityManager);
        hibernateSearch.reindexAll(false);
    }

    @Test
    public void testSearch() {
        saveNovel();
        List<NovelInfo> novelInfoList = hibernateSearch.search("Keyword1");

        assertNotNull(novelInfoList);

        novelInfoList = hibernateSearch.search(new String[]{"Keyword1"}, new String[]{"keyword"});

        assertNotNull(novelInfoList);

        novelInfoList = hibernateSearch.search("*");

        assertNotNull(novelInfoList);
    }

    @Test(expected = SearchException.class)
    public void testSearchException() {
        hibernateSearch.search(new String[]{""}, new String[]{""});
    }

    @Test
    public void testPaged() {
        saveNovel();
        List<NovelInfo> novelList = hibernateSearch.searchList("Keyword1", 0, 10);
        Long novelCount = hibernateSearch.searchCount("Keyword1");

        Page<NovelInfo> pagedUser = new PageImpl<>(novelList, PageRequest.of(0, 10), novelCount);

        assertEquals(1, pagedUser.getTotalPages());
        assertEquals(1, pagedUser.getTotalElements());
        assertEquals(1, pagedUser.getContent().size());
    }

    @Test
    public void testFacet() {
        saveNovel();
        List<Facet> novelFacet = hibernateSearch.facet("keywordSet.keyword", 2);

        assertNotNull(novelFacet);
        assertEquals(2, novelFacet.size());
    }

    private void saveNovel() {
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

        dao.save(novel);
        hibernateSearch.reindex();
    }
}