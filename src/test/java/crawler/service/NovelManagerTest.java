package crawler.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import crawler.dao.NovelDao;
import crawler.domain.Novel;
import crawler.domain.NovelChapter;
import crawler.domain.NovelChapterInfo;
import crawler.domain.NovelInfo;

public class NovelManagerTest extends BaseManagerTestCase {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private NovelDao novelDao;

    @Autowired
    private NovelManager novelManager;

    @Autowired
    private NovelOutputManager novelOutputManager;

    private GreenMail greenMail;

    @BeforeEach
    public void setUp() {
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();

        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(greenMail.getSmtp().getPort());
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

        novelDao.save(novel);
    }

    @AfterEach
    public void tearDown() {
        greenMail.stop();
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
    public void testSendUnreadReport() throws Exception {
        greenMail.purgeEmailFromAllMailboxes();

        novelOutputManager.sendUnreadReport();

        assertTrue(greenMail.getReceivedMessages().length == 1);
    }

    @Test
    public void testSendModifiedDateList() throws Exception {
        greenMail.purgeEmailFromAllMailboxes();

        novelOutputManager.sendModifiedDateReport();

        assertTrue(greenMail.getReceivedMessages().length == 1);
    }
}
