package crawler.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class NovelChapterTest {

    @Test
    public void testEquals() {
        NovelChapter novelChapter1 = new NovelChapter();
        novelChapter1.setUrl("test1");

        assertTrue(novelChapter1.equals(novelChapter1));
        assertFalse(novelChapter1.equals(null));

        NovelChapter novelChapter2 = new NovelChapter();

        assertFalse(novelChapter1.equals(novelChapter2));

        novelChapter2.setUrl("test2");

        assertFalse(novelChapter1.equals(novelChapter2));

        novelChapter2.setUrl("test1");

        assertTrue(novelChapter1.equals(novelChapter2));
    }
}
