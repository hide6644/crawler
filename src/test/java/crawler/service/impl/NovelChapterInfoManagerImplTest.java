package crawler.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import crawler.entity.NovelChapter;
import crawler.mapping.NovelChapterSource;
import crawler.mapping.NovelIndexElement;

public class NovelChapterInfoManagerImplTest {

    @Test
    public void testSaveNovelInfo() throws Exception {
        NovelIndexElement novelIndexElement = mock(NovelIndexElement.class);
        NovelChapterSource novelChapterSource = mock(NovelChapterSource.class);
        when(novelIndexElement.getChapterModifiedDate()).thenReturn("2016/09/23 00:00");
        when(novelChapterSource.getNovelChapter()).thenReturn(new NovelChapter());

        NovelChapterInfoManagerImpl novelChapterInfoManager = new NovelChapterInfoManagerImpl();

        assertDoesNotThrow(() -> {
            novelChapterInfoManager.saveNovelChapterInfo(novelIndexElement, novelChapterSource);
        });
    }
}
