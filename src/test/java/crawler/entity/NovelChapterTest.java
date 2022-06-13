package crawler.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NovelChapterTest {

    @Test
    void testEquals() {
        NovelChapter novelChapter1 = new NovelChapter();
        novelChapter1.setUrl("test1");

        assertEquals(novelChapter1, novelChapter1);
        assertNotEquals(null, novelChapter1);

        NovelChapter novelChapter2 = new NovelChapter();

        assertNotEquals(novelChapter1, novelChapter2);

        novelChapter2.setUrl("test2");

        assertNotEquals(novelChapter1, novelChapter2);

        novelChapter2.setUrl("test1");

        assertEquals(novelChapter1, novelChapter2);
    }

    @Test
    void testNovelChapterHistories() {
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
    void testHashCode() {
        NovelChapter novelChapter1 = new NovelChapter();
        novelChapter1.setUrl("test1");

        assertEquals(novelChapter1.hashCode(), novelChapter1.hashCode());
    }
}
