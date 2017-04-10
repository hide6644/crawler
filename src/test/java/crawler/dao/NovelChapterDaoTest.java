package crawler.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import crawler.domain.NovelChapter;

public class NovelChapterDaoTest extends BaseDaoTestCase {

    @Autowired
    private NovelChapterDao dao;

    @Test
    public void testGetByUrl() throws Exception {
        NovelChapter novelChapter = dao.getByUrl("http://www.foo.bar/test/");

        assertNull(novelChapter);
    }
}
