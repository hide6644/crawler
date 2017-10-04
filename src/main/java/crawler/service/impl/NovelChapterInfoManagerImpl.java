package crawler.service.impl;

import org.springframework.stereotype.Service;

import crawler.domain.NovelChapterInfo;
import crawler.domain.source.NovelChapterModifiedDate;
import crawler.domain.source.NovelChapterSource;
import crawler.domain.source.NovelIndexElement;
import crawler.service.NovelChapterInfoManager;

/**
 * 小説の章の付随情報を管理する.
 */
@Service("novelChapterInfoManager")
public class NovelChapterInfoManagerImpl extends GenericManagerImpl<NovelChapterInfo, Long> implements NovelChapterInfoManager {

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
