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
    public void testGetNovelsByCheckedDate() {
        List<Novel> novelList = dao.getByCheckedDateLessThanEqual(new Date());

        assertNotNull(novelList);
    }

    @Test
    public void testGetNovelsByUnread() {
        List<Novel> novelList = dao.getByUnreadTrueOrderByTitleAndId();

        assertNotNull(novelList);
    }
}
