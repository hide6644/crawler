package crawler.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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

    @Test
    public void testHashCode() {
        NovelChapter novelChapter1 = new NovelChapter();
        novelChapter1.setUrl("test1");

        assertTrue(novelChapter1.hashCode() == novelChapter1.hashCode());
    }
}
