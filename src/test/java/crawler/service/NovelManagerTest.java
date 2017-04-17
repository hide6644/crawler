package crawler.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.subethamail.wiser.Wiser;

import crawler.domain.Novel;

public class NovelManagerTest extends BaseManagerTestCase {

    @Autowired
    private NovelManager novelManager;

    @Before
    public void onSetUp() {
        smtpPort = smtpPort + (int) (Math.random() * 100);

        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(smtpPort);
        mailSender.setHost("localhost");
    }

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

    @Test
    public void testSendReport() throws Exception {
        Wiser wiser = new Wiser();
        wiser.setPort(smtpPort);
        wiser.start();

        novelManager.sendReport();

        wiser.stop();

        assertTrue(wiser.getMessages().size() == 1);
    }
}
