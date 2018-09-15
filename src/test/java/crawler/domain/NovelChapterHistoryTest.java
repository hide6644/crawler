package crawler.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NovelChapterHistoryTest {

    @Test
    public void testEquals() {
        NovelChapterHistory novelChapterHistory1 = new NovelChapterHistory();
        novelChapterHistory1.setId(1L);

        assertTrue(novelChapterHistory1.equals(novelChapterHistory1));
        assertFalse(novelChapterHistory1.equals(null));

        NovelChapterHistory novelChapterHistory2 = new NovelChapterHistory();

        assertFalse(novelChapterHistory1.equals(novelChapterHistory2));

        novelChapterHistory2.setId(2L);

        assertFalse(novelChapterHistory1.equals(novelChapterHistory2));

        novelChapterHistory2.setId(1L);

        assertTrue(novelChapterHistory1.equals(novelChapterHistory2));
   }
}
