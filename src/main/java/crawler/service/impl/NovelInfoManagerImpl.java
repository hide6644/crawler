package crawler.service.impl;

import org.springframework.stereotype.Service;

import crawler.domain.source.NovelInfoSource;
import crawler.domain.source.NovelSource;
import crawler.exception.NovelNotFoundException;
import crawler.service.NovelInfoManager;

/**
 * 小説の付随情報を管理する.
 */
@Service("novelInfoManager")
public class NovelInfoManagerImpl extends BaseManagerImpl implements NovelInfoManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveNovelInfo(final NovelSource novelSource) throws NovelNotFoundException {
        NovelInfoSource novelInfoSource = NovelInfoSource.newInstance(novelSource.getNovelInfoUrl(), novelSource.getNovel().getNovelInfo());

        novelInfoSource.getNovelInfo().setNovel(novelSource.getNovel());
        novelSource.getNovel().setNovelInfo(novelInfoSource.getNovelInfo());
    }
}
