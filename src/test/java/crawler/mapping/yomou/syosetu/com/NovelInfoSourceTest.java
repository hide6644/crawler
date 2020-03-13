package crawler.mapping.yomou.syosetu.com;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import crawler.entity.NovelInfo;
import crawler.mapping.yomou.syosetu.com.NovelInfoSource;

public class NovelInfoSourceTest {

    @Test
    public void testGetNovelInfo() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/testInfo.html").getPath();

        NovelInfoSource novelInfoSource = NovelInfoSource.newInstance("file://" + filePath, null);
        NovelInfo novelInfo = novelInfoSource.getNovelInfo();

        assertNotNull(novelInfo);
        assertFalse(novelInfo.getCheckEnable());

        filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/testInfo_new.html").getPath();
        novelInfoSource = NovelInfoSource.newInstance("file://" + filePath, novelInfo);
        novelInfo = novelInfoSource.getNovelInfo();

        assertNotNull(novelInfo);
        assertTrue(novelInfo.getCheckEnable());
    }

    @Test
    public void testGetNovelInfoFinished() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/testInfo_finished.html").getPath();
        NovelInfoSource novelInfoSource = NovelInfoSource.newInstance("file://" + filePath, null);
        NovelInfo novelInfo = novelInfoSource.getNovelInfo();

        assertNotNull(novelInfo);
        assertTrue(novelInfo.isFinished());
    }
}
