package crawler.util;

import org.jsoup.Jsoup;
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

    @Test
    public void testProxyConnect() throws Exception {
        Assertions.assertDoesNotThrow(() -> {
            NovelManagerUtil.proxyConnect(Jsoup.connect("http://localhost:19999/test"));
        });
    }

    @Test
    public void testProxyAuth() throws Exception {
        Assertions.assertDoesNotThrow(() -> {
            NovelManagerUtil.proxyAuth(Jsoup.connect("http://localhost:19999/test"));
        });
    }
}
