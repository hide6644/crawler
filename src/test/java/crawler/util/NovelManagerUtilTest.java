package crawler.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import crawler.exception.NovelConnectException;
import crawler.exception.NovelNotFoundException;

class NovelManagerUtilTest {

    @Test
    void testGetUrl() throws Exception {
        System.setProperty("http_proxy", "");
        assertThrows(NovelNotFoundException.class, () -> {
            NovelManagerUtil.getUrl("test");
        });
    }

    @Test
    void testGetSource() throws Exception {
        System.setProperty("http_proxy", "");
        assertThrows(NovelNotFoundException.class, () -> {
            NovelManagerUtil.getSource("http://localhost:19999/test");
        });
    }

    @Test
    void testProxyGetSource() throws Exception {
        System.setProperty("http_proxy", "http://foo.bar:8080");
        assertThrows(NovelConnectException.class, () -> {
            NovelManagerUtil.getSource("http://localhost:19999/test");
        });
    }

    @Test
    void testProxyAuthGetSource() throws Exception {
        System.setProperty("http_proxy", "http://hoge:piyo@foo.bar:8080");
        assertThrows(NovelConnectException.class, () -> {
            NovelManagerUtil.getSource("http://localhost:19999/test");
        });
    }
}
