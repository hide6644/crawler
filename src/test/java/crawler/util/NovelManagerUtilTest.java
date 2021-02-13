package crawler.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import crawler.exception.NovelNotFoundException;

public class NovelManagerUtilTest {

    @Test
    public void testGetUrl() throws Exception {
        System.setProperty("http_proxy", "");
        assertThrows(NovelNotFoundException.class, () -> {
            NovelManagerUtil.getUrl("test");
        });
    }

    @Test
    public void testGetSource() throws Exception {
        System.setProperty("http_proxy", "");
        assertThrows(NovelNotFoundException.class, () -> {
            NovelManagerUtil.getSource("http://localhost:19999/test");
        });
    }

//    @Test
//    public void testProxyGetSource() throws Exception {
//        System.setProperty("http_proxy", "http://foo.bar:8080");
//        assertThrows(NovelConnectException.class, () -> {
//            NovelManagerUtil.getSource("http://localhost:19999/test");
//        });
//    }
//
//    @Test
//    public void testProxyAuthGetSource() throws Exception {
//        System.setProperty("http_proxy", "http://hoge:piyo@foo.bar:8080");
//        assertThrows(NovelConnectException.class, () -> {
//            NovelManagerUtil.getSource("http://localhost:19999/test");
//        });
//    }
}
