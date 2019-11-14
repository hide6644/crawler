package crawler.service.impl;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import crawler.dto.NovelChapterSource;
import crawler.dto.NovelIndexElement;
import crawler.entity.NovelChapter;

public class NovelChapterInfoManagerImplTest {

    @Test
    public void testSaveNovelInfo() throws Exception {
        NovelIndexElement novelIndexElement = mock(NovelIndexElement.class);
        NovelChapterSource novelChapterSource = mock(NovelChapterSource.class);
        when(novelIndexElement.getChapterModifiedDate()).thenReturn("2016/09/23 00:00");
        when(novelChapterSource.getNovelChapter()).thenReturn(new NovelChapter());

        NovelChapterInfoManagerImpl novelChapterInfoManager = new NovelChapterInfoManagerImpl();

        Assertions.assertDoesNotThrow(() -> {
            novelChapterInfoManager.saveNovelChapterInfo(novelIndexElement, novelChapterSource);
        });
    }
}
