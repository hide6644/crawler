package crawler.service.impl;

import static org.mockito.BDDMockito.*;

import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import crawler.dao.NovelDao;
import crawler.domain.Novel;
import crawler.domain.NovelInfo;
import crawler.service.NovelChapterManager;
import crawler.service.NovelInfoManager;

public class NovelManagerImplTest extends BaseManagerMockTestCase {

    @Mock
    private Logger log;

    @Mock
    private NovelDao novelDao;

    @Mock
    private NovelInfoManager novelInfoManager;

    @Mock
    private NovelChapterManager novelChapterManager;

    @InjectMocks
    private NovelManagerImpl novelManager = new NovelManagerImpl();

    @Test
    public void testAddAndDelete() {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        // 登録対象有り
        novelManager.save("file://" + filePath);

        Novel novel = new Novel();
        novel.setUrl("file://" + filePath);
        novel.setTitle("Test小説のタイトル");
        novel.setWritername("Test作者名");
        novel.setDescription("Test小説の説明");
        novel.setBody("Test本文");

        given(novelDao.getByUrl("file://" + filePath)).willReturn(novel);

        // 登録済み
        novelManager.save("file://" + filePath);

        // 削除
        novelManager.delete("file://" + filePath);

        // 登録対象無し
        novelManager.save("file://" + filePath + "test");
    }

    @Test
    public void testCheckForUpdatesAndSaveHistory() {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        Novel novel = new Novel();
        novel.setUrl("file://" + filePath);
        novel.setTitle("Test小説のタイトル");
        novel.setWritername("Test作者名");
        novel.setDescription("Test小説の説明");
        novel.setBody("Test本文");

        given(novelDao.get(1L)).willReturn(novel);

        // 更新対象有り
        novelManager.checkForUpdatesAndSaveHistory(1L);

        // 更新対象無し
        novelManager.checkForUpdatesAndSaveHistory(2L);

        novel.setUrl("file://" + filePath + "test");
        // 更新対象無し
        novelManager.checkForUpdatesAndSaveHistory(novel);
    }

    @Test
    public void testFavorite() {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        Novel novel = new Novel();
        novel.setUrl("file://" + filePath);
        novel.setTitle("Test小説のタイトル");
        novel.setWritername("Test作者名");
        novel.setDescription("Test小説の説明");
        novel.setBody("Test本文");
        NovelInfo novelInfo = new NovelInfo();
        novel.setNovelInfo(novelInfo);

        given(novelDao.getByUrl("file://" + filePath)).willReturn(novel);

        novelManager.favorite("file://" + filePath, true);

        novelManager.favorite("file://" + filePath, false);
    }
}
