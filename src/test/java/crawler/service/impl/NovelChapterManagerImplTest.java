package crawler.service.impl;

import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import crawler.dao.NovelChapterDao;
import crawler.domain.source.NovelSource;
import crawler.service.NovelChapterInfoManager;

public class NovelChapterManagerImplTest extends BaseManagerMockTestCase {

    @Mock
    private Logger log;

    @Mock
    private NovelChapterDao novelChapterDao;

    @Mock
    private NovelChapterInfoManager novelChapterInfoManager;

    @InjectMocks
    private NovelChapterManagerImpl novelChapterManager = new NovelChapterManagerImpl();

    @Test
    public void testSaveAllNovelChapter() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        NovelSource novelSource = NovelSource.newInstance("file://" + filePath);
        novelSource.setHostname("file://" + filePath.substring(0, filePath.indexOf("test.html")));

        novelChapterManager.saveAllNovelChapter(novelSource);
    }
}
