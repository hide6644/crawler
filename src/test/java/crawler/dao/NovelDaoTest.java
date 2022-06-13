package crawler.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import crawler.entity.Novel;

class NovelDaoTest extends BaseDaoTestCase {

    @Autowired
    private NovelDao dao;

    @BeforeEach
    void setUp() {
        Novel novel = new Novel();
        novel.setUrl("Url");
        novel.setTitle("Title");
        novel.setWritername("Writername");
        novel.setDescription("Description");
        novel.setBody("Body");
        novel.setDeleted(false);
        dao.save(novel);
    }

    @Test
    void testFindByUrl() {
        Novel novel = dao.findByUrl("Url");

        assertNotNull(novel);

        // 削除
        dao.delete(novel);

        assertNull(dao.findByUrl("Url"));
    }

    @Test
    void testFindByDeletedFalseAndCheckedDateLessThanEqualAndCheckEnableTrue() {
        Stream<Novel> novelList = dao.findByDeletedFalseAndCheckedDateLessThanEqualAndCheckEnableTrue(LocalDateTime.now());

        assertNotNull(novelList);

        novelList.close();
    }

    @Test
    void testFindByUnreadTrueOrderByTitleAndNovelChapterId() {
        Stream<Novel> novelList = dao.findByUnreadTrueOrderByTitleAndNovelChapterId();

        assertNotNull(novelList);

        novelList.close();
    }

    @Test
    void testFindByDeletedFalseOrderByTitle() {
        Stream<Novel> novelList = dao.findByDeletedFalseOrderByTitle();

        assertNotNull(novelList);

        novelList.close();
    }
}
