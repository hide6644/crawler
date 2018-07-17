package crawler.service.impl;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;

import crawler.domain.Novel;
import crawler.domain.source.NovelSource;

public class NovelInfoManagerImplTest {

    @Test
    public void testSaveNovelInfo() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/testInfo.html").getPath();

        NovelSource novelSource = mock(NovelSource.class);
        when(novelSource.getNovelInfoUrl()).thenReturn("file://" + filePath);
        when(novelSource.getNovel()).thenReturn(new Novel());

        NovelInfoManagerImpl novelInfoManager = new NovelInfoManagerImpl();
        novelInfoManager.saveNovelInfo(novelSource);
    }
}
