package crawler.domain.source;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import crawler.domain.NovelChapterInfo;

public class NovelChapterInfoElementTest {

    @Test
    public void testGetChapterElementList() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        NovelSource novelSource = new NovelSource("file://" + filePath);
        novelSource.mapping();
        List<NovelBodyElement> novelBodyElementList = novelSource.getChapterElementList();

        NovelBodyElement novelBodyElement = novelBodyElementList.get(0);
        NovelChapterInfoElement novelChapterInfoElement = new NovelChapterInfoElement(novelBodyElement.getElement());
        novelChapterInfoElement.mapping();
        NovelChapterInfo novelChapterInfo = novelChapterInfoElement.getNovelChapterInfo();

        assertNotNull(novelChapterInfo);

        novelChapterInfoElement.setNovelChapterInfo(novelChapterInfo);
        novelChapterInfoElement.mapping();
    }
}
