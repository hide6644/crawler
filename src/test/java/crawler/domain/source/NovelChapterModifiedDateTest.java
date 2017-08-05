package crawler.domain.source;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import crawler.domain.NovelChapterInfo;

public class NovelChapterModifiedDateTest {

    @Test
    public void testGetNovelChapterInfo() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        NovelSource novelSource = NovelSourceFactory.newInstance("file://" + filePath);
        List<NovelIndexElement> novelIndexList = novelSource.getNovelIndexList();
        NovelIndexElement novelIndexElement = novelIndexList.get(0);

        NovelChapterModifiedDate novelChapterModifiedDate = NovelChapterModifiedDateFactory.newInstance(novelIndexElement, null);
        NovelChapterInfo novelChapterInfo = novelChapterModifiedDate.getNovelChapterInfo();

        assertNotNull(novelChapterInfo);

        novelChapterModifiedDate = NovelChapterModifiedDateFactory.newInstance(novelIndexElement, null);
        novelChapterInfo = novelChapterModifiedDate.getNovelChapterInfo();

        assertNotNull(novelChapterInfo);
    }
}
