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

        NovelSource novelSource = NovelSource.newInstance("file://" + filePath);
        List<NovelIndexElement> novelIndexList = novelSource.getNovelIndexList();

        assertNotNull(novelIndexList);
        assertEquals(3, novelIndexList.size());
        assertTrue(novelSource.isAdd());
    }

    @Test
    public void testGetChapterHistoryElementSet() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        NovelSource novelSource = NovelSource.newInstance("file://" + filePath);
        Novel novel = novelSource.getNovel();

        filePath = this.getClass().getClassLoader().getResource("novel/20160925/test.html").getPath();

        novelSource = NovelSource.newInstance("file://" + filePath, novel);

        Set<NovelIndexElement> novelHistoryIndexSet = novelSource.getNovelHistoryIndexSet();

        assertNotNull(novelHistoryIndexSet);
        assertEquals(3, novelHistoryIndexSet.size());
        assertFalse(novelSource.isAdd());
    }

    @Test
    public void testGetNovelInfoLink() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        NovelSource novelSource = NovelSource.newInstance("file://" + filePath);
        String novelInfoUrl = novelSource.getNovelInfoUrl();

        assertNotNull(novelInfoUrl);
        assertEquals("../testinfo.html", novelInfoUrl);

        String hostUrl = novelSource.getHostname();

        assertNotNull(hostUrl);
        assertEquals("file://", hostUrl);
    }
}
