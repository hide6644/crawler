package crawler.domain.source;

import static org.junit.Assert.*;

import org.junit.Test;

import crawler.domain.NovelInfo;

public class NovelInfoSourceTest {

    @Test
    public void testGetNovelInfo() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/testInfo.html").getPath();

        NovelInfoSource novelInfoSource = NovelInfoSourceFactory.newInstance("file://" + filePath, null);
        NovelInfo novelInfo = novelInfoSource.getNovelInfo();

        assertNotNull(novelInfo);

        novelInfoSource = NovelInfoSourceFactory.newInstance("file://" + filePath, novelInfo);
        novelInfo = novelInfoSource.getNovelInfo();

        assertNotNull(novelInfo);
    }
}
