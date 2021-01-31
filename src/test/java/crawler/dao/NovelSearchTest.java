package crawler.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import crawler.entity.Novel;
import crawler.entity.NovelInfo;

public class NovelSearchTest extends BaseDaoTestCase {

    @Autowired
    private NovelDao dao;

    @Autowired
    private NovelSearch novelSearch;

    @BeforeEach
    public void setUp() {
        novelSearch.reindexAll(false);
    }

    @Test
    public void testSearch() {
        saveNovel();

        assertNotNull(novelSearch.search(null));
        assertNotNull(novelSearch.search("Keyword1"));
    }

    @Test
    public void testFacet() {
        saveNovel();

        Map<String, Long> userFacet = novelSearch.facet("novelInfo.keywordFacet", 2);

        assertNotNull(userFacet);
        assertEquals(2, userFacet.size());
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
        novelSearch.reindex();
    }
}
