package crawler.domain.source;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import crawler.domain.Novel;
import net.htmlparser.jericho.Source;

public class NovelSourceTest {

    @Test
    public void testGetChapterElementList() throws Exception {
        String fileName = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();
        File file = new File(fileName);
        String url = "http://www.foo.bar/20160924/";
        NovelSource novelSource = new NovelSource(new URL(url), new Source(file));
        novelSource.mapping();
        List<NovelBodyElement> novelBodyElementList = novelSource.getChapterElementList();

        assertNotNull(novelBodyElementList);
        assertEquals(novelBodyElementList.size(), 10);
        assertTrue(novelSource.isAdd());
    }

    @Test
    public void testGetChapterHistoryElementSet() throws Exception {
        String fileName = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();
        File file = new File(fileName);
        String url = "http://www.foo.bar/20160924/";
        NovelSource novelSource = new NovelSource(new URL(url), new Source(file));
        novelSource.mapping();
        Novel novel = novelSource.getNovel();

        fileName = this.getClass().getClassLoader().getResource("novel/20160925/test.html").getPath();
        file = new File(fileName);
        url = "http://www.foo.bar/20160925/";
        novelSource = new NovelSource(new URL(url), new Source(file));
        novelSource.setNovel(novel);
        novelSource.mapping();

        Set<NovelBodyElement> novelBodyElementSet = novelSource.getChapterHistoryElementSet();

        assertNotNull(novelBodyElementSet);
        assertEquals(novelBodyElementSet.size(), 10);
        assertFalse(novelSource.isAdd());
    }

    @Test
    public void testGetNovelInfoLink() throws Exception {
        String fileName = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();
        File file = new File(fileName);
        String url = "http://www.foo.bar/20160924/";
        NovelSource novelSource = new NovelSource(new URL(url), new Source(file));
        novelSource.mapping();
        String novelInfoLink = novelSource.getNovelInfoLink();

        assertNotNull(novelInfoLink);
        assertEquals(novelInfoLink, "http://ncode.syosetu.com/novelview/infotop/ncode/20160924/");

        String hostUrl = novelSource.getHostUrl();

        assertNotNull(hostUrl);
        assertEquals(hostUrl, "http://www.foo.bar");
    }
}
