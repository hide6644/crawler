package crawler.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import crawler.dao.NovelDao;
import crawler.entity.Novel;
import crawler.entity.NovelChapter;
import crawler.entity.NovelChapterInfo;
import crawler.entity.NovelInfo;

class NovelManagerTest extends BaseManagerTestCase {

    @Autowired
    private NovelDao novelDao;

    @Autowired
    private NovelManager novelManager;

    @Autowired
    private NovelOutputManager novelOutputManager;

    private static GreenMail greenMail;

    @BeforeAll
    static void setUpClass() {
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();
    }

    @BeforeEach
    void setUp() throws FolderException {
        greenMail.purgeEmailFromAllMailboxes();
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
        novel.addNovelChapter(novelChapter);

        NovelChapterInfo novelChapterInfo = new NovelChapterInfo();
        novelChapterInfo.setCheckedDate(LocalDateTime.now().minusDays(1));
        novelChapterInfo.setModifiedDate(LocalDateTime.now().minusDays(2));
        novelChapterInfo.setUnread(true);
        novelChapterInfo.setNovelChapter(novelChapter);
        novelChapter.setNovelChapterInfo(novelChapterInfo);

        novelDao.save(novel);
    }

    @AfterAll
    static void tearDownClass() {
        greenMail.stop();
    }

    @Test
    void testGetCheckTargetId() {
        List<Long> checkTargetId = novelManager.getCheckTargetId();

        assertNotNull(checkTargetId);
    }

    @Test
    void testGetUnreadNovels() {
        List<Novel> unreadNovels = novelOutputManager.getUnreadNovels();

        assertNotNull(unreadNovels);
    }

    @Test
    void testGetNovelsIncludingModifiedDate() {
        List<Novel> novels = novelOutputManager.getModifiedDateOfNovels();

        assertNotNull(novels);
    }

    @Test
    void testSendUnreadReport() {
        novelOutputManager.sendUnreadReport();

        assertEquals(1, greenMail.getReceivedMessages().length);
    }

    @Test
    void testSendModifiedDateList() {
        novelOutputManager.sendModifiedDateReport();

        assertEquals(1, greenMail.getReceivedMessages().length);
    }
}
