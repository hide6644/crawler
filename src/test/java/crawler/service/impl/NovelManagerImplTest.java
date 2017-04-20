package crawler.service.impl;

import static org.mockito.BDDMockito.*;

import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import crawler.dao.NovelDao;
import crawler.domain.Novel;
import crawler.domain.source.NovelSource;
import crawler.service.NovelChapterManager;
import crawler.service.NovelInfoManager;

public class NovelManagerImplTest extends BaseManagerMockTestCase {

    @Mock
    private NovelSource novelSource;

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
    public void testAdd() throws Exception {
        String fileName = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        novelManager.add("file://" + fileName);
    }

    @Test
    public void testCheckForUpdatesAndSaveHistory() throws Exception {
        String fileName = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        Novel novel = new Novel();
        novel.setUrl("file://" + fileName);

        given(novelDao.get(1L)).willReturn(novel);

        novelManager.checkForUpdatesAndSaveHistory(1L);
        novelManager.checkForUpdatesAndSaveHistory(2L);
    }
}
