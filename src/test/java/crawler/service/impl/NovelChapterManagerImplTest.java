package crawler.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import crawler.dao.NovelChapterDao;
import crawler.dao.NovelChapterHistoryDao;
import crawler.entity.NovelChapter;
import crawler.mapping.yomou.syosetu.com.NovelSource;
import crawler.service.NovelChapterInfoManager;

class NovelChapterManagerImplTest extends BaseManagerMockTestCase {

    @Mock
    private Logger log;

    @Mock
    private NovelChapterDao novelChapterDao;

    @Mock
    private NovelChapterHistoryDao novelChapterHistoryDao;

    @Mock
    private NovelChapterInfoManager novelChapterInfoManager;

    @InjectMocks
    private NovelChapterManagerImpl novelChapterManager = new NovelChapterManagerImpl();

    @Test
    void testSaveAllNovelChapter() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/20160924/test.html").getPath();

        NovelSource novelSource = NovelSource.newInstance("file://" + filePath);
        novelSource.setHostname("file://" + filePath.substring(0, filePath.indexOf("test.html")));

        NovelChapter novelChapter =new NovelChapter();
        novelChapter.setUrl("Url");
        novelChapter.setTitle("Title");
        novelChapter.setBody("Body");
        novelChapter.setCreateDate(LocalDateTime.now());
        novelChapter.setUpdateDate(LocalDateTime.now());

        when(novelChapterDao.findByUrl(novelSource.getHostname() + "test01.html")).thenReturn(novelChapter);

        assertDoesNotThrow(() -> {
            novelChapterManager.saveAllNovelChapter(novelSource);
        });
        novelSource.getNovel().getNovelChapters()
                .forEach(novelChapter2 -> {
                    novelChapter2.getNovelChapterHistories().forEach(novelChapterHistory -> {
                        novelChapterHistory.getTitle();
                        novelChapterHistory.getBody();
                        novelChapterHistory.getNovelChapter();
                    });
                });
    }
}
