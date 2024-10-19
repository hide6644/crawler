package crawler.service.impl;

import org.springframework.stereotype.Service;

import crawler.exception.NovelConnectException;
import crawler.exception.NovelNotFoundException;
import crawler.mapping.yomou.syosetu.com.NovelInfoSource;
import crawler.mapping.yomou.syosetu.com.NovelSource;
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
    public void saveNovelInfo(final NovelSource novelSource) throws NovelConnectException, NovelNotFoundException {
        var novelInfoSource = NovelInfoSource.newInstance(novelSource.getNovelInfoUrl(), novelSource.getNovel().getNovelInfo());

        novelInfoSource.getNovelInfo().setNovel(novelSource.getNovel());
        novelSource.getNovel().setNovelInfo(novelInfoSource.getNovelInfo());
    }
}
