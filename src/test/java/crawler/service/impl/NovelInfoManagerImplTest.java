package crawler.service.impl;

import static org.mockito.Mockito.*;

import org.junit.Test;

import crawler.domain.Novel;
import crawler.domain.source.NovelSource;

public class NovelInfoManagerImplTest {

    @Test
    public void testSaveNovelInfo() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/testinfo.html").getPath();

        NovelSource novelSource = mock(NovelSource.class);
        when(novelSource.getNovelInfoLink()).thenReturn("file://" + filePath);
        when(novelSource.getNovel()).thenReturn(new Novel());

        NovelInfoManagerImpl novelInfoManager = new NovelInfoManagerImpl();
        novelInfoManager.saveNovelInfo(novelSource);
    }
}
