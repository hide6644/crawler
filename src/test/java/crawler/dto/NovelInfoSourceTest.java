package crawler.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import crawler.entity.NovelInfo;
import crawler.mapping.yomou.syosetu.com.NovelInfoSource;

public class NovelInfoSourceTest {

    @Test
    public void testGetNovelInfo() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/testInfo.html").getPath();

        NovelInfoSource novelInfoSource = NovelInfoSource.newInstance("file://" + filePath, null);
        NovelInfo novelInfo = novelInfoSource.getNovelInfo();

        assertNotNull(novelInfo);
        assertFalse(novelInfo.getCheckEnable());

        filePath = this.getClass().getClassLoader().getResource("novel/testInfo_new.html").getPath();
        novelInfoSource = NovelInfoSource.newInstance("file://" + filePath, novelInfo);
        novelInfo = novelInfoSource.getNovelInfo();

        assertNotNull(novelInfo);
        assertTrue(novelInfo.getCheckEnable());
    }
}
