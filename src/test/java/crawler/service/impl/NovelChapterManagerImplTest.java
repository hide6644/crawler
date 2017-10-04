package crawler.service.impl;

import java.net.URL;

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
    public void testSaveNovelChapter() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        NovelSource novelSource = NovelSource.newInstance("file://" + filePath);

        int startIndex = 0;
        if (filePath.indexOf(":") >= 0) {
            startIndex = 3;
        }

        String fileQuery = filePath.substring(startIndex, filePath.indexOf("test.html"));
        URL url = new URL("file", fileQuery, "test.html");
        novelSource.setUrl(url);
        novelChapterManager.saveNovelChapter(novelSource);
    }
}
