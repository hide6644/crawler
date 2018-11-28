package crawler.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import crawler.exception.NovelNotFoundException;

public class NovelManagerUtilTest {

    @Test
    public void testGetUrl() throws Exception {
        Assertions.assertThrows(NovelNotFoundException.class, () -> {
            NovelManagerUtil.getUrl("test");
        });
    }

    @Test
    public void testGetSource() throws Exception {
        Assertions.assertThrows(NovelNotFoundException.class, () -> {
            NovelManagerUtil.getSource("http://localhost:19999/test");
        });
    }
}
