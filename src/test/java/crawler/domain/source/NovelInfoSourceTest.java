package crawler.domain.source;

import static org.junit.Assert.*;

import org.junit.Test;

import crawler.domain.NovelInfo;

public class NovelInfoSourceTest {

    @Test
    public void testGetChapterElementList() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/testInfo.html").getPath();

        NovelInfoSource novelInfoSource = new NovelInfoSource("file://" + filePath);
        novelInfoSource.mapping();
        NovelInfo novelInfo = novelInfoSource.getNovelInfo();

        assertNotNull(novelInfo);

        novelInfoSource.setNovelInfo(novelInfo);
        novelInfoSource.mapping();
    }
}
