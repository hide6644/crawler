package crawler.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import crawler.entity.Novel;
import crawler.entity.NovelChapter;
import crawler.entity.NovelChapterInfo;
import crawler.entity.Role;
import crawler.entity.User;
import crawler.entity.UserNovelInfo;

public class UserDaoTest extends BaseDaoTestCase {

    @Autowired
    private UserDao userDao;

    @Autowired
    private NovelDao novelDao;

    @BeforeEach
    public void setUp() {
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
        novel.setDeleted(false);

        NovelChapter novelChapter1 = new NovelChapter();
        novelChapter1.setUrl("ChapterUrl1");
        novelChapter1.setTitle("ChapterTitle1");
        novelChapter1.setBody("ChapterBody1");
        NovelChapter novelChapter2 = new NovelChapter();
        novelChapter2.setUrl("ChapterUrl2");
        novelChapter2.setTitle("ChapterTitle2");
        novelChapter2.setBody("ChapterBody2");

        novel.addNovelChapter(novelChapter1);
        novel.addNovelChapter(novelChapter2);
        novelChapter1.setNovel(novel);
        novelChapter2.setNovel(novel);

        NovelChapterInfo novelChapterInfo1 = new NovelChapterInfo();
        novelChapterInfo1.setUnread(true);
        NovelChapterInfo novelChapterInfo2 = new NovelChapterInfo();
        novelChapterInfo2.setUnread(true);

        novelChapter1.setNovelChapterInfo(novelChapterInfo1);
        novelChapter2.setNovelChapterInfo(novelChapterInfo2);
        novelChapterInfo1.setNovelChapter(novelChapter1);
        novelChapterInfo2.setNovelChapter(novelChapter2);

        novelDao.save(novel);

        UserNovelInfo userNovelInfo = new UserNovelInfo();
        userNovelInfo.setUser(user);
        userNovelInfo.setNovel(novel);

        user.addUserNovelInfo(userNovelInfo);
        novel.addUserNovelInfo(userNovelInfo);

        userDao.save(user);
    }

    @AfterEach
    public void tearDown() {
        userDao.deleteAll();
    }

    @Test
    public void testFindByUnreadTrueOrderByTitleAndNovelChapterId() {
        Stream<User> userList = userDao.findByUnreadTrueOrderByTitleAndNovelChapterId();

        assertNotNull(userList);

        userList.forEach(user -> {
            assertEquals("hoge", user.getUsername());

            user.getUserNovelInfos().forEach(userNovelInfo -> {
                List<NovelChapter> novelChapters = userNovelInfo.getNovel().getNovelChapters();

                assertEquals("ChapterTitle1", novelChapters.get(0).getTitle());
                assertEquals("ChapterTitle2", novelChapters.get(1).getTitle());
            });
        });

        userList.close();
    }
}
