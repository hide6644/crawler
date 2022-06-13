package crawler.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;

import crawler.entity.Novel;
import crawler.mapping.yomou.syosetu.com.NovelSource;

class NovelInfoManagerImplTest {

    @Test
    void testSaveNovelInfo() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/testInfo.html").getPath();

        NovelSource novelSource = mock(NovelSource.class);
        when(novelSource.getNovelInfoUrl()).thenReturn("file://" + filePath);
        when(novelSource.getNovel()).thenReturn(new Novel());

        NovelInfoManagerImpl novelInfoManager = new NovelInfoManagerImpl();
        assertDoesNotThrow(() -> {
            novelInfoManager.saveNovelInfo(novelSource);
        });
    }
}
