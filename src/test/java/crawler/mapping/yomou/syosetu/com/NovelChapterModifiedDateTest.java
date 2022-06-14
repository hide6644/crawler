package crawler.mapping.yomou.syosetu.com;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import crawler.entity.NovelChapterInfo;

class NovelChapterModifiedDateTest {

    @Test
    void testGetNovelChapterInfo() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/20160924/test.html").getPath();

        NovelSource novelSource = NovelSource.newInstance("file://" + filePath);
        List<NovelIndexElement> novelIndexList = novelSource.getNovelIndexList().collect(Collectors.toList());
        NovelIndexElement novelIndexElement = novelIndexList.get(0);

        NovelChapterModifiedDate novelChapterModifiedDate = NovelChapterModifiedDate.newInstance(novelIndexElement, null);
        NovelChapterInfo novelChapterInfo = novelChapterModifiedDate.getNovelChapterInfo();

        assertNotNull(novelChapterInfo);
        assertNotEquals(novelIndexElement, novelIndexList.get(1));

        filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/20160925/test.html").getPath();

        novelSource = NovelSource.newInstance("file://" + filePath);
        novelIndexList = novelSource.getNovelIndexList().collect(Collectors.toList());
        novelIndexElement = novelIndexList.get(0);

        novelChapterModifiedDate = NovelChapterModifiedDate.newInstance(novelIndexElement, novelChapterInfo);

        assertNotNull(novelChapterInfo);
        assertNotEquals(novelIndexElement, novelIndexList.get(1));
    }
}
