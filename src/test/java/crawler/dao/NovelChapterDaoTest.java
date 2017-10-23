package crawler.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import crawler.domain.Novel;
import crawler.domain.NovelChapter;

public class NovelChapterDaoTest extends BaseDaoTestCase {

    @Autowired
    private NovelDao dao;

    @Autowired
    private NovelChapterDao cdao;

    @Before
    public void setUp() {
        Novel novel = new Novel();
        novel.setUrl("Url");
        novel.setTitle("Title");
        novel.setWritername("Writername");
        novel.setDescription("Description");
        novel.setBody("Body");
        novel.setDeleted(false);

        NovelChapter novelChapter = new NovelChapter();
        novelChapter.setUrl("Url");
        novelChapter.setTitle("Title");
        novelChapter.setBody("Body");
        novelChapter.setNovel(novel);
        novel.addNovelChapter(novelChapter);
        dao.save(novel);
    }

    @Test
    public void testGetByUrl() {
        NovelChapter novelChapter = cdao.getByUrl("Url");

        assertNotNull(novelChapter);

        // 削除
        dao.remove(novelChapter.getNovel().getId());

        assertNull(cdao.getByUrl("Url"));
    }
}
