package crawler.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
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
import crawler.dao.UserDao;
import crawler.entity.Novel;
import crawler.entity.NovelChapter;
import crawler.entity.NovelChapterInfo;
import crawler.entity.NovelInfo;
import crawler.entity.Role;
import crawler.entity.User;
import crawler.entity.UserNovelInfo;

public class NovelManagerTest extends BaseManagerTestCase {

    @Autowired
    private UserDao userDao;

    @Autowired
    private NovelDao novelDao;

    @Autowired
    private NovelManager novelManager;

    @Autowired
    private NovelOutputManager novelOutputManager;

    private static GreenMail greenMail;

    @BeforeAll
    public static void setUpClass() {
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();
    }

    @BeforeEach
    public void setUp() throws FolderException {
        greenMail.purgeEmailFromAllMailboxes();
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(greenMail.getSmtp().getPort());
        mailSender.setHost("localhost");

        User user = new User();
        user.setUsername("hoge");
        user.setPassword("hoge");
        user.setEmail("hoge@foo.bar");
        user.setEnabled(true);
        user.setRoles(Arrays.asList(Role.ROLE_USER));

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

        UserNovelInfo userNovelInfo = new UserNovelInfo();
        userNovelInfo.setUser(user);
        userNovelInfo.setNovel(novel);

        user.addUserNovelInfo(userNovelInfo);
        novel.addUserNovelInfo(userNovelInfo);

        userDao.save(user);
    }

    @AfterAll
    public static void tearDownClass() {
        greenMail.stop();
    }

    @Test
    public void testGetCheckTargetId() {
        List<Long> checkTargetId = novelManager.getCheckTargetId();

        assertNotNull(checkTargetId);
    }

    @Test
    public void testGetUnreadNovels() {
        List<User> unreadNovels = novelOutputManager.getUnreadUserNovels();

        assertNotNull(unreadNovels);
    }

    @Test
    public void testGetNovelsIncludingModifiedDate() {
        List<Novel> novels = novelOutputManager.getModifiedDateOfNovels();

        assertNotNull(novels);
    }

    @Test
    public void testSendUnreadReport() {
        novelOutputManager.sendUnreadReport();

        assertTrue(greenMail.getReceivedMessages().length == 1);
    }

    @Test
    public void testSendModifiedDateList() {
        novelOutputManager.sendModifiedDateReport();

        assertTrue(greenMail.getReceivedMessages().length == 1);
    }
}
