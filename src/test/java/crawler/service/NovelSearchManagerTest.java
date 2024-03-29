package crawler.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import crawler.dao.NovelDao;
import crawler.entity.Novel;
import crawler.entity.NovelChapter;
import crawler.entity.NovelChapterInfo;
import crawler.entity.NovelInfo;

class NovelSearchManagerTest extends BaseManagerTestCase {

    @Autowired
    private NovelDao novelDao;

    @Autowired
    private NovelSearchManager novelSearchManager;

    @BeforeEach
    void setUp() {
        novelSearchManager.reindexAll(false);
        Novel novel = new Novel();
        novel.setUrl("Url");
        novel.setTitle("Title");
        novel.setWritername("Writername");
        novel.setDescription("Description");
        novel.setBody("Body");

        NovelInfo novelInfo = new NovelInfo();
        novelInfo.setCreateDate(LocalDateTime.now());
        novelInfo.setModifiedDate(LocalDateTime.now());
        novelInfo.setFavorite(true);
        novelInfo.setKeyword("Keyword1 Keyword2");
        novelInfo.setNovel(novel);
        novel.setNovelInfo(novelInfo);

        NovelChapter novelChapter =new NovelChapter();
        novelChapter.setUrl("Url");
        novelChapter.setTitle("Title");
        novelChapter.setBody("Body");
        novelChapter.setCreateDate(LocalDateTime.now());
        novelChapter.setUpdateDate(LocalDateTime.now());
        novel.addNovelChapter(novelChapter);

        NovelChapterInfo novelChapterInfo = new NovelChapterInfo();
        novelChapterInfo.setCheckedDate(LocalDateTime.now().minusDays(1));
        novelChapterInfo.setModifiedDate(LocalDateTime.now().minusDays(2));
        novelChapterInfo.setUnread(true);
        novelChapterInfo.setNovelChapter(novelChapter);
        novelChapter.setNovelChapterInfo(novelChapterInfo);

        novelDao.save(novel);
    }

    @Test
    void testSearch() {
        novelSearchManager.reindexAll(false);
        List<Novel> novelList = novelSearchManager.search(null);
        assertTrue(novelList.size() > 0);

        novelList = novelSearchManager.search("TEST");
        assertEquals(0, novelList.size());
    }
}
