package crawler.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.facet.Facet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import crawler.dao.jpa.HibernateSearchImpl;
import crawler.dto.SearchTermAndField;
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
        SearchTermAndField term = new SearchTermAndField();
        if (term.isEmpty()) {
            assertNotNull(hibernateSearch.search("*").getResultStream());
            assertNotNull(hibernateSearch.search("Keyword1").getResultStream());
        }

        term.addTermAndField("Keyword1", "keyword");
        if (!term.isEmpty()) {
            assertNotNull(hibernateSearch.search(term.getTermToArray(), term.getFieldToArray(), new Occur[]{Occur.SHOULD}).getResultStream());
            assertNotNull(hibernateSearch.search(term.getTermToArray(), term.getFieldToArray()).getResultStream());
        }
    }

    @Test
    public void testSearchException() {
        Assertions.assertThrows(SearchException.class, () -> {
            hibernateSearch.search(new String[]{""}, new String[]{""}, new Occur[]{Occur.SHOULD});
        });
        Assertions.assertThrows(SearchException.class, () -> {
            hibernateSearch.search(new String[]{""}, new String[]{""});
        });
    }

    @Test
    public void testPaged() {
        saveNovel();
        org.apache.lucene.search.Sort sort = new org.apache.lucene.search.Sort(new SortField("keywordSort", SortField.Type.STRING));
        FullTextQuery novelList = hibernateSearch.search("Keyword1", 0L, 10, sort);

        assertEquals(1, novelList.getResultStream().count());
        assertEquals(Integer.valueOf(1), novelList.getResultSize());
    }

    @Test
    public void testPagedByField() {
        saveNovel();
        SearchTermAndField term = new SearchTermAndField();
        term.addTermAndField("Keyword1", "keyword");
        org.apache.lucene.search.Sort sort = new org.apache.lucene.search.Sort(new SortField("keywordSort", SortField.Type.STRING));
        FullTextQuery novelList = hibernateSearch.search(term.getTermToArray(), term.getFieldToArray(), 0L, 10, sort);

        assertEquals(1, novelList.getResultStream().count());
        assertEquals(Integer.valueOf(1), novelList.getResultSize());
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
