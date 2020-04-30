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
    public void testNovelChapterHistories() {
        NovelChapter novelChapter = new NovelChapter();
        NovelChapterHistory novelChapterHistory1 = new NovelChapterHistory();
        novelChapterHistory1.setId(1L);
        novelChapter.addNovelChapterHistory(novelChapterHistory1);

        assertEquals(1, novelChapter.getNovelChapterHistories().size());

        NovelChapterHistory novelChapterHistory2 = new NovelChapterHistory();
        novelChapterHistory2.setId(1L);
        novelChapter.removeNovelChapterHistory(novelChapterHistory2);

        assertEquals(0, novelChapter.getNovelChapterHistories().size());
    }

    @Test
    public void testHashCode() {
        NovelChapter novelChapter1 = new NovelChapter();
        novelChapter1.setUrl("test1");

        assertTrue(novelChapter1.hashCode() == novelChapter1.hashCode());
    }
}
