package crawler.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import crawler.domain.Novel;

public class NovelDaoTest extends BaseDaoTestCase {

    @Autowired
    private NovelDao dao;

    @Test
    public void testGetNovelsByCheckedDate() throws Exception {
        List<Novel> novelList = dao.getByCheckedDateLessThanEqual(new Date());

        assertNotNull(novelList);
    }

    @Test
    public void testGetNovelsByUnread() throws Exception {
        List<Novel> novelList = dao.getByUnreadTrueOrderByTitleAndId();

        assertNotNull(novelList);
    }
}
