package crawler.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NovelHistoryTest {

    @Test
    void testEquals() {
        NovelHistory novelHistory1 = new NovelHistory();
        novelHistory1.setId(1L);

        assertEquals(novelHistory1, novelHistory1);
        assertNotEquals(null, novelHistory1);

        NovelHistory novelHistory2 = new NovelHistory();

        assertNotEquals(novelHistory1, novelHistory2);

        novelHistory2.setId(2L);

        assertNotEquals(novelHistory1, novelHistory2);

        novelHistory2.setId(1L);

        assertEquals(novelHistory1, novelHistory2);
   }
}
