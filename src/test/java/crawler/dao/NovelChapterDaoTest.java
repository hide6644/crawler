package crawler.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import crawler.entity.Novel;
import crawler.entity.NovelChapter;

public class NovelChapterDaoTest extends BaseDaoTestCase {

    @Autowired
    private NovelDao dao;

    @Autowired
    private NovelChapterDao cdao;

    @BeforeEach
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
    public void testFindByUrl() {
        NovelChapter novelChapter = cdao.findByUrl("Url");

        assertNotNull(novelChapter);

        // 削除
        dao.deleteById(novelChapter.getNovel().getId());

        assertNull(cdao.findByUrl("Url"));
    }
}
