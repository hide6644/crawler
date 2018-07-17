package crawler.service.impl;

import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import crawler.dao.NovelChapterDao;
import crawler.domain.NovelChapter;
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

        NovelChapter novelChapter =new NovelChapter();
        novelChapter.setUrl("Url");
        novelChapter.setTitle("Title");
        novelChapter.setBody("Body");
        novelChapter.setCreateDate(LocalDateTime.now());
        novelChapter.setUpdateDate(LocalDateTime.now());

        when(novelChapterDao.findByUrl(novelSource.getHostname() + "test01.html")).thenReturn(novelChapter);

        novelChapterManager.saveAllNovelChapter(novelSource);
    }
}
