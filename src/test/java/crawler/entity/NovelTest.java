package crawler.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NovelTest {

    @Test
    void testEquals() {
        Novel novel1 = new Novel();
        novel1.setUrl("test1");

        assertEquals(novel1, novel1);
        assertNotEquals(null, novel1);

        Novel novel2 = new Novel();

        assertNotEquals(novel1, novel2);

        novel2.setUrl("test2");

        assertNotEquals(novel1, novel2);

        novel2.setUrl("test1");

        assertEquals(novel1, novel2);
    }

    @Test
    void testNovelChapters() {
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
    void testNovelHistories() {
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
    void testHashCode() {
        Novel novel1 = new Novel();
        novel1.setUrl("test1");

        assertEquals(novel1.hashCode(), novel1.hashCode());
    }
}
