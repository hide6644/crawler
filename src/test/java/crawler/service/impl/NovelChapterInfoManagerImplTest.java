package crawler.service.impl;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import crawler.domain.NovelChapter;
import crawler.domain.source.NovelChapterSource;
import crawler.domain.source.NovelIndexElement;

public class NovelChapterInfoManagerImplTest {

    @Test
    public void testSaveNovelInfo() throws Exception {
        NovelIndexElement novelIndexElement = mock(NovelIndexElement.class);
        NovelChapterSource novelChapterSource = mock(NovelChapterSource.class);
        when(novelIndexElement.getChapterModifiedDate()).thenReturn("2016/09/23 00:00");
        when(novelChapterSource.getNovelChapter()).thenReturn(new NovelChapter());

        NovelChapterInfoManagerImpl novelChapterInfoManager = new NovelChapterInfoManagerImpl();
        novelChapterInfoManager.saveNovelChapterInfo(novelIndexElement, novelChapterSource);
    }
}
