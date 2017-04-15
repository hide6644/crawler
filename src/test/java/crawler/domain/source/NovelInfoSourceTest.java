package crawler.domain.source;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.junit.Test;

import crawler.domain.NovelInfo;
import net.htmlparser.jericho.Source;

public class NovelInfoSourceTest {

    @Test
    public void testGetChapterElementList() throws Exception {
        String fileName = this.getClass().getClassLoader().getResource("novel/testInfo.html").getPath();
        File file = new File(fileName);
        String url = "http://www.foo.bar/testInfo/";
        NovelInfoSource novelInfoSource = new NovelInfoSource(new URL(url), new Source(file));
        novelInfoSource.mapping();
        NovelInfo novelInfo = novelInfoSource.getNovelInfo();

        assertNotNull(novelInfo);

        fileName = this.getClass().getClassLoader().getResource("novel/testInfo.html").getPath();
        file = new File(fileName);
        url = "http://www.foo.bar/testInfo/";
        novelInfoSource = new NovelInfoSource(new URL(url), new Source(file));
        novelInfoSource.setNovelInfo(novelInfo);
        novelInfoSource.mapping();
    }
}
