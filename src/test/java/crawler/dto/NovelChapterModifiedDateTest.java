package crawler.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import crawler.entity.NovelChapterInfo;
import crawler.mapping.yomou.syosetu.com.NovelChapterModifiedDate;
import crawler.mapping.yomou.syosetu.com.NovelIndexElement;
import crawler.mapping.yomou.syosetu.com.NovelSource;

public class NovelChapterModifiedDateTest {

    @Test
    public void testGetNovelChapterInfo() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        NovelSource novelSource = NovelSource.newInstance("file://" + filePath);
        List<NovelIndexElement> novelIndexList = novelSource.getNovelIndexList().collect(Collectors.toList());
        NovelIndexElement novelIndexElement = novelIndexList.get(0);

        NovelChapterModifiedDate novelChapterModifiedDate = NovelChapterModifiedDate.newInstance(novelIndexElement, null);
        NovelChapterInfo novelChapterInfo = novelChapterModifiedDate.getNovelChapterInfo();

        assertNotNull(novelChapterInfo);
        assertNotEquals(novelIndexElement, novelIndexList.get(1));
    }
}
