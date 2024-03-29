package crawler.mapping.yomou.syosetu.com;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import crawler.entity.Novel;

class NovelSourceTest {

    @Test
    void testGetChapterElementList() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/20160924/test.html").getPath();

        NovelSource novelSource = NovelSource.newInstance("file://" + filePath);
        Stream<NovelIndexElement> novelIndexList = novelSource.getNovelIndexList();

        assertNotNull(novelIndexList);
        assertEquals(3, novelIndexList.count());
        assertTrue(novelSource.isAdd());
}

    @Test
    void testGetChapterHistoryElementSet() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/20160924/test.html").getPath();

        NovelSource novelSource = NovelSource.newInstance("file://" + filePath);
        Novel novel = novelSource.getNovel();

        filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/20160925/test.html").getPath();

        novelSource = NovelSource.newInstance("file://" + filePath, novel);

        Set<NovelIndexElement> novelHistoryIndexSet = novelSource.getNovelHistoryIndexSet();

        assertNotNull(novelHistoryIndexSet);
        assertEquals(3, novelHistoryIndexSet.size());
        assertFalse(novelSource.isAdd());

        novelSource = NovelSource.newInstance("file://" + filePath, novelSource.getNovel());

        novelHistoryIndexSet = novelSource.getNovelHistoryIndexSet();

        assertNotNull(novelHistoryIndexSet);
        assertEquals(0, novelHistoryIndexSet.size());
        assertFalse(novelSource.isAdd());
    }

    @Test
    void testGetNovelInfoLink() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/20160924/test.html").getPath();

        NovelSource novelSource = NovelSource.newInstance("file://" + filePath);
        String novelInfoUrl = novelSource.getNovelInfoUrl();

        assertNotNull(novelInfoUrl);
        assertEquals("../testinfo.html", novelInfoUrl);

        String hostUrl = novelSource.getHostname();

        assertNotNull(hostUrl);
        assertEquals("file://", hostUrl);
    }
}
