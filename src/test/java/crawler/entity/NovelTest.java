package crawler.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NovelTest {

    @Test
    public void testEquals() {
        Novel novel1 = new Novel();
        novel1.setUrl("test1");

        assertTrue(novel1.equals(novel1));
        assertFalse(novel1.equals(null));

        Novel novel2 = new Novel();

        assertFalse(novel1.equals(novel2));

        novel2.setUrl("test2");

        assertFalse(novel1.equals(novel2));

        novel2.setUrl("test1");

        assertTrue(novel1.equals(novel2));
    }

    @Test
    public void testNovelChapters() {
        Novel novel = new Novel();
        NovelChapter novelChapter1 = new NovelChapter();
        novelChapter1.setUrl("test1");
        novel.addNovelChapter(novelChapter1);

        assertEquals(1, novel.getNovelChapters().size());

        NovelChapter novelChapter2 = new NovelChapter();
        novelChapter2.setUrl("test1");
        novel.removeNovelChapter(novelChapter2);

        assertEquals(0, novel.getNovelChapters().size());
    }

    @Test
    public void testNovelHistories() {
        Novel novel = new Novel();
        NovelHistory novelHistory1 = new NovelHistory();
        novelHistory1.setId(1L);
        novel.addNovelHistory(novelHistory1);

        assertEquals(1, novel.getNovelHistories().size());

        NovelHistory novelHistory2 = new NovelHistory();
        novelHistory2.setId(1L);
        novel.removeNovelHistory(novelHistory2);

        assertEquals(0, novel.getNovelHistories().size());
    }

    @Test
    public void testHashCode() {
        Novel novel1 = new Novel();
        novel1.setUrl("test1");

        assertTrue(novel1.hashCode() == novel1.hashCode());
    }
}
