package crawler.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.hibernate.search.query.facet.Facet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import crawler.dao.jpa.HibernateSearchImpl;
import crawler.entity.Novel;
import crawler.entity.NovelInfo;

public class HibernateSearchTest extends BaseDaoTestCase {

    @Autowired
    private NovelDao dao;

    private HibernateSearch<NovelInfo> hibernateSearch;

    @BeforeEach
    public void setUp() {
        hibernateSearch = new HibernateSearchImpl<NovelInfo>(NovelInfo.class, entityManager);
        hibernateSearch.reindexAll(false);
    }

    @Test
    public void testSearch() {
        saveNovel();
        Stream<NovelInfo> novelInfoList = hibernateSearch.search("Keyword1");

        assertNotNull(novelInfoList);

        novelInfoList = hibernateSearch.search(new String[]{"Keyword1"}, new String[]{"keyword"});

        assertNotNull(novelInfoList);

        novelInfoList = hibernateSearch.search("*");

        assertNotNull(novelInfoList);
    }

    @Test
    public void testSearchException() {
        Assertions.assertThrows(SearchException.class, () -> {
            hibernateSearch.search(new String[]{""}, new String[]{""});
        });
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
