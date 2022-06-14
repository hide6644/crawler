package crawler.service.impl;

import org.springframework.stereotype.Service;

import crawler.mapping.yomou.syosetu.com.NovelChapterModifiedDate;
import crawler.mapping.yomou.syosetu.com.NovelChapterSource;
import crawler.mapping.yomou.syosetu.com.NovelIndexElement;
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
        var novelChapterModifiedDate = NovelChapterModifiedDate.newInstance(novelIndexElement, novelChapterSource.getNovelChapter().getNovelChapterInfo());

        novelChapterModifiedDate.getNovelChapterInfo().setNovelChapter(novelChapterSource.getNovelChapter());
        novelChapterSource.getNovelChapter().setNovelChapterInfo(novelChapterModifiedDate.getNovelChapterInfo());
    }
}
