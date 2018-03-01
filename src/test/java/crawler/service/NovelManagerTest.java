package crawler.service;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.subethamail.wiser.Wiser;

import crawler.domain.Novel;
import crawler.domain.NovelChapter;
import crawler.domain.NovelChapterInfo;
import crawler.domain.NovelInfo;

public class NovelManagerTest extends BaseManagerTestCase {

    @Autowired
    private NovelManager novelManager;

    @Autowired
    private NovelOutputManager novelOutputManager;

    @Before
    public void setUp() {
        smtpPort = smtpPort + new Random().nextInt(100);

        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(smtpPort);
        mailSender.setHost("localhost");

        Novel novel = new Novel();
        novel.setUrl("Url");
        novel.setTitle("Title");
        novel.setWritername("Writername");
        novel.setDescription("Description");
        novel.setBody("Body");

        NovelInfo novelInfo = new NovelInfo();
        novelInfo.setCreateDate(LocalDateTime.now());
        novelInfo.setModifiedDate(LocalDateTime.now());
        novelInfo.setFavorite(true);
        novelInfo.setKeyword("Keyword1 Keyword2");
        novelInfo.setNovel(novel);
        novel.setNovelInfo(novelInfo);

        NovelChapter novelChapter =new NovelChapter();
        novelChapter.setUrl("Url");
        novelChapter.setTitle("Title");
        novelChapter.setBody("Body");
        novelChapter.setCreateDate(LocalDateTime.now());
        novelChapter.setUpdateDate(LocalDateTime.now());
        novelChapter.setNovel(novel);
        novel.addNovelChapter(novelChapter);

        NovelChapterInfo novelChapterInfo = new NovelChapterInfo();
        novelChapterInfo.setCheckedDate(LocalDateTime.now().minusDays(1));
        novelChapterInfo.setModifiedDate(LocalDateTime.now().minusDays(2));
        novelChapterInfo.setUnread(true);
        novelChapterInfo.setNovelChapter(novelChapter);
        novelChapter.setNovelChapterInfo(novelChapterInfo);

        novelManager.save(novel);
    }

    @Test
    public void testGetCheckTargetId() {
        List<Long> checkTargetId = novelManager.getCheckTargetId();

        assertNotNull(checkTargetId);
    }

    @Test
    public void testGetUnreadNovels() {
        List<Novel> unreadNovels = novelOutputManager.getUnreadNovels();

        assertNotNull(unreadNovels);
    }

    @Test
    public void testGetNovelsIncludingModifiedDate() {
        List<Novel> novels = novelOutputManager.getModifiedDateOfNovels();

        assertNotNull(novels);
    }

    @Test
    public void testSendUnreadReport() {
        Wiser wiser = new Wiser();
        wiser.setPort(smtpPort);
        wiser.start();

        novelOutputManager.sendUnreadReport();

        wiser.stop();

        assertTrue(wiser.getMessages().size() == 1);
    }

    @Test
    public void testSendModifiedDateList() {
        Wiser wiser = new Wiser();
        wiser.setPort(smtpPort);
        wiser.start();

        novelOutputManager.sendModifiedDateReport();

        wiser.stop();

        assertTrue(wiser.getMessages().size() == 1);
    }
}
