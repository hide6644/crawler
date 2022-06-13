package crawler.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import crawler.dao.NovelDao;
import crawler.dao.NovelHistoryDao;
import crawler.entity.Novel;
import crawler.service.NovelChapterManager;
import crawler.service.NovelInfoManager;

class NovelManagerImplTest extends BaseManagerMockTestCase {

    @Mock
    private Logger log;

    @Mock
    private NovelDao novelDao;

    @Mock
    private NovelHistoryDao novelHistoryDao;

    @Mock
    private NovelInfoManager novelInfoManager;

    @Mock
    private NovelChapterManager novelChapterManager;

    @InjectMocks
    private NovelManagerImpl novelManager = new NovelManagerImpl();

    @Test
    void testAddAndDelete() {
        String filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/20160924/test.html").getPath();

        // 登録対象有り
        assertDoesNotThrow(() -> {
            novelManager.save("file://" + filePath);
        });

        Novel novel = new Novel();
        novel.setUrl("file://" + filePath);
        novel.setTitle("Test小説のタイトル");
        novel.setWritername("Test作者名");
        novel.setDescription("Test小説の説明");
        novel.setBody("Test本文");

        given(novelDao.findByUrl("file://" + filePath)).willReturn(novel);

        // 登録有りのため、更新
        assertDoesNotThrow(() -> {
            novelManager.save("file://" + filePath);
        });

        // 削除
        assertDoesNotThrow(() -> {
            novelManager.delete("file://" + filePath);
        });

        // 登録対象無し
        assertDoesNotThrow(() -> {
            novelManager.save("file://" + filePath + "test");
        });

        // 接続不可
        assertDoesNotThrow(() -> {
            novelManager.save("http://localhost:19999/test");
        });
    }

    @Test
    void testCheckForUpdatesAndSaveHistory() {
        String filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/20160924/test.html").getPath();

        Novel novel = new Novel();
        novel.setUrl("file://" + filePath);
        novel.setTitle("Test小説のタイトル");
        novel.setWritername("Test作者名");
        novel.setDescription("Test小説の説明");
        novel.setBody("Test本文");

        given(novelDao.findById(1L)).willReturn(Optional.ofNullable(novel));

        // 更新対象有り
        assertDoesNotThrow(() -> {
            novelManager.checkForUpdatesAndSaveHistory(1L);
        });

        // 更新対象無し
        assertDoesNotThrow(() -> {
            novelManager.checkForUpdatesAndSaveHistory(2L);
        });

        novel.setUrl("file://" + filePath + "test");
        // 更新対象無し
        assertDoesNotThrow(() -> {
            novelManager.checkForUpdatesAndSaveHistory(novel);
            novel.getNovelHistories().forEach(novelHistory -> {
                novelHistory.getTitle();
                novelHistory.getWritername();
                novelHistory.getDescription();
                novelHistory.getNovel();
            });
        });

        novel.setUrl("http://localhost:19999/test");
        // 接続不可
        assertDoesNotThrow(() -> {
            novelManager.checkForUpdatesAndSaveHistory(novel);
        });
    }
}
