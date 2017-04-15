package crawler.domain.source;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.junit.Test;

import crawler.domain.NovelChapter;
import net.htmlparser.jericho.Source;

public class NovelChapterSourceTest {

    @Test
    public void testIsAdd() throws Exception {
        String fileName = this.getClass().getClassLoader().getResource("novel/20160924/test01.html").getPath();
        File file = new File(fileName);
        String url = "http://www.foo.bar/20160924/1/";
        NovelChapterSource novelChapterSource = new NovelChapterSource(new URL(url), new Source(file));
        novelChapterSource.mapping();
        NovelChapter novelChapter = novelChapterSource.getNovelChapter();

        assertTrue(novelChapterSource.isAdd());

        fileName = this.getClass().getClassLoader().getResource("novel/20160925/test01.html").getPath();
        file = new File(fileName);
        url = "http://www.foo.bar/20160924/1/";
        novelChapterSource = new NovelChapterSource(new URL(url), new Source(file));
        novelChapterSource.setNovelChapter(novelChapter);
        novelChapterSource.mapping();

        assertFalse(novelChapterSource.isAdd());
    }
}
