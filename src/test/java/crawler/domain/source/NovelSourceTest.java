package crawler.domain.source;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import crawler.domain.Novel;

public class NovelSourceTest {

    @Test
    public void testGetChapterElementList() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        NovelSource novelSource = new NovelSource("file://" + filePath);
        novelSource.mapping();
        List<NovelBodyElement> novelBodyElementList = novelSource.getChapterElementList();

        assertNotNull(novelBodyElementList);
        assertEquals(novelBodyElementList.size(), 3);
        assertTrue(novelSource.isAdd());
    }

    @Test
    public void testGetChapterHistoryElementSet() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        NovelSource novelSource = new NovelSource("file://" + filePath);
        novelSource.mapping();
        Novel novel = novelSource.getNovel();

        filePath = this.getClass().getClassLoader().getResource("novel/20160925/test.html").getPath();

        novelSource = new NovelSource("file://" + filePath);
        novelSource.setNovel(novel);
        novelSource.mapping();

        Set<NovelBodyElement> novelBodyElementSet = novelSource.getChapterHistoryElementSet();

        assertNotNull(novelBodyElementSet);
        assertEquals(novelBodyElementSet.size(), 3);
        assertFalse(novelSource.isAdd());
    }

    @Test
    public void testGetNovelInfoLink() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        NovelSource novelSource = new NovelSource("file://" + filePath);
        novelSource.mapping();
        String novelInfoLink = novelSource.getNovelInfoLink();

        assertNotNull(novelInfoLink);
        assertEquals(novelInfoLink, "http://ncode.syosetu.com/novelview/infotop/ncode/20160924/");

        String hostUrl = novelSource.getHostUrl();

        assertNotNull(hostUrl);
        assertEquals(hostUrl, "file://");
    }
}
