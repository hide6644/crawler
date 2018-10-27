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
    public void testHashCode() {
        Novel novel1 = new Novel();
        novel1.setUrl("test1");

        assertTrue(novel1.hashCode() == novel1.hashCode());
    }
}
