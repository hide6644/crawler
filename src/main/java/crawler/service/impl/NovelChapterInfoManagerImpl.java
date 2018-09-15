package crawler.service.impl;

import org.springframework.stereotype.Service;

import crawler.dto.NovelChapterModifiedDate;
import crawler.dto.NovelChapterSource;
import crawler.dto.NovelIndexElement;
import crawler.service.NovelChapterInfoManager;

/**
 * 小説の章の付随情報を管理する.
 */
@Service("novelChapterInfoManager")
public class NovelChapterInfoManagerImpl extends BaseManagerImpl implements NovelChapterInfoManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveNovelChapterInfo(final NovelIndexElement novelIndexElement, final NovelChapterSource novelChapterSource) {
        NovelChapterModifiedDate novelChapterModifiedDate = NovelChapterModifiedDate.newInstance(novelIndexElement, novelChapterSource.getNovelChapter().getNovelChapterInfo());

        novelChapterModifiedDate.getNovelChapterInfo().setNovelChapter(novelChapterSource.getNovelChapter());
        novelChapterSource.getNovelChapter().setNovelChapterInfo(novelChapterModifiedDate.getNovelChapterInfo());
    }
}
