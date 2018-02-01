package crawler.dao;

import static org.junit.Assert.*;

import java.util.Date;
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
    public void testGetByUrl() {
        Novel novel = dao.getByUrl("Url");

        assertNotNull(novel);

        // 削除
        dao.remove(novel);
        novel = dao.getByUrl("Url");

        assertNull(novel);
    }

    @Test
    public void testGetByCheckedDateLessThanEqualAndCheckEnableTrue() {
        List<Novel> novelList = dao.getByCheckedDateLessThanEqualAndCheckEnableTrue(new Date());

        assertNotNull(novelList);
    }

    @Test
    public void testGetByUnreadTrueOrderByTitleAndNovelChapterId() {
        List<Novel> novelList = dao.getByUnreadTrueOrderByTitleAndNovelChapterId();

        assertNotNull(novelList);
    }

    @Test
    public void testGetOrderByTitle() {
        List<Novel> novelList = dao.getOrderByTitle();

        assertNotNull(novelList);
    }
}
