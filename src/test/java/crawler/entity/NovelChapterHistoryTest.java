package crawler.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NovelChapterHistoryTest {

    @Test
    void testEquals() {
        NovelChapterHistory novelChapterHistory1 = new NovelChapterHistory();
        novelChapterHistory1.setId(1L);

        assertEquals(novelChapterHistory1, novelChapterHistory1);
        assertNotEquals(null, novelChapterHistory1);

        NovelChapterHistory novelChapterHistory2 = new NovelChapterHistory();

        assertNotEquals(novelChapterHistory1, novelChapterHistory2);

        novelChapterHistory2.setId(2L);

        assertNotEquals(novelChapterHistory1, novelChapterHistory2);

        novelChapterHistory2.setId(1L);

        assertEquals(novelChapterHistory1, novelChapterHistory2);
   }
}
