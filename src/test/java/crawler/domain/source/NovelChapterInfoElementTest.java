package crawler.domain.source;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.Test;

import crawler.domain.NovelChapterInfo;
import net.htmlparser.jericho.Source;

public class NovelChapterInfoElementTest {

    @Test
    public void testGetChapterElementList() throws Exception {
        String fileName = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();
        File file = new File(fileName);
        String url = "http://www.foo.bar/20160924/";
        NovelSource novelSource = new NovelSource(new URL(url), new Source(file));
        novelSource.mapping();
        List<NovelBodyElement> novelBodyElementList = novelSource.getChapterElementList();

        NovelBodyElement novelBodyElement = novelBodyElementList.get(0);
        NovelChapterInfoElement novelChapterInfoElement = new NovelChapterInfoElement(novelBodyElement.getElement());
        novelChapterInfoElement.mapping();
        NovelChapterInfo novelChapterInfo = novelChapterInfoElement.getNovelChapterInfo();

        assertNotNull(novelChapterInfo);

        fileName = this.getClass().getClassLoader().getResource("novel/20160925/test.html").getPath();
        file = new File(fileName);
        url = "http://www.foo.bar/20160925/";
        novelSource = new NovelSource(new URL(url), new Source(file));
        novelSource.mapping();
        novelBodyElementList = novelSource.getChapterElementList();

        novelBodyElement = novelBodyElementList.get(0);
        novelChapterInfoElement = new NovelChapterInfoElement(novelBodyElement.getElement());
        novelChapterInfoElement.setNovelChapterInfo(novelChapterInfo);
        novelChapterInfoElement.mapping();
    }
}
