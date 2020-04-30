package crawler.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NovelHistoryTest {

    @Test
    public void testEquals() {
        NovelHistory novelHistory1 = new NovelHistory();
        novelHistory1.setId(1L);

        assertTrue(novelHistory1.equals(novelHistory1));
        assertFalse(novelHistory1.equals(null));

        NovelHistory novelHistory2 = new NovelHistory();

        assertFalse(novelHistory1.equals(novelHistory2));

        novelHistory2.setId(2L);

        assertFalse(novelHistory1.equals(novelHistory2));

        novelHistory2.setId(1L);

        assertTrue(novelHistory1.equals(novelHistory2));
   }
}
