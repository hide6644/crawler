package crawler.dao;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import crawler.domain.Novel;

public class NovelDaoTest extends BaseDaoTestCase {

    @Autowired
    private NovelDao dao;

    @Before
    public void setUp() {
        Novel novel = new Novel();
        novel.setUrl("Url");
        novel.setTitle("Title");
        novel.setWritername("Writername");
        novel.setDescription("Description");
        novel.setBody("Body");
        novel.setDeleted(false);
        dao.save(novel);
    }

    @Test
    public void testFindByUrl() {
        Novel novel = dao.findByUrl("Url");

        assertNotNull(novel);

        // 削除
        dao.delete(novel);

        assertNull(dao.findByUrl("Url"));
    }

    @Test
    public void testFindByDeletedFalseAndCheckedDateLessThanEqualAndCheckEnableTrue() {
        List<Novel> novelList = dao.findByDeletedFalseAndCheckedDateLessThanEqualAndCheckEnableTrue(LocalDateTime.now());

        assertNotNull(novelList);
    }

    @Test
    public void testFindByUnreadTrueOrderByTitleAndNovelChapterId() {
        List<Novel> novelList = dao.findByUnreadTrueOrderByTitleAndNovelChapterId();

        assertNotNull(novelList);
    }

    @Test
    public void testFindByDeletedFalseOrderByTitle() {
        List<Novel> novelList = dao.findByDeletedFalseOrderByTitle();

        assertNotNull(novelList);
    }
}
