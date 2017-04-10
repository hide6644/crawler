package crawler.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import crawler.domain.Novel;

public class NovelManagerTest extends BaseManagerTestCase {

    @Autowired
    private NovelManager novelManager;

    @Test
    public void testGetCheckTargetId() throws Exception {
        List<Long> checkTargetId = novelManager.getCheckTargetId();

        assertNotNull(checkTargetId);
    }

    @Test
    public void testGetUnreadNovels() throws Exception {
        List<Novel> unreadNovels = novelManager.getUnreadNovels();

        assertNotNull(unreadNovels);
    }

    public void testSendReport() throws Exception {
        novelManager.sendReport();
    }
}
