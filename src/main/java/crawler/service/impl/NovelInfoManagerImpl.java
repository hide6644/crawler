package crawler.service.impl;

import org.springframework.stereotype.Service;

import crawler.domain.NovelInfo;
import crawler.domain.source.NovelInfoSource;
import crawler.domain.source.NovelSource;
import crawler.service.NovelInfoManager;

/**
 * 小説の付随情報を管理する.
 */
@Service("novelInfoManager")
public class NovelInfoManagerImpl extends GenericManagerImpl<NovelInfo, Long> implements NovelInfoManager {

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelInfoManager#saveNovelInfo(crawler.domain.source.NovelSource)
     */
    public void saveNovelInfo(final NovelSource novelSource) {
        NovelInfoSource novelInfoSource = new NovelInfoSource(novelSource.getHtml());
        novelInfoSource.setNovelInfo(novelSource.getNovel().getNovelInfo());
        novelInfoSource.mapping();

        novelInfoSource.getNovelInfo().setNovel(novelSource.getNovel());
        novelSource.getNovel().setNovelInfo(novelInfoSource.getNovelInfo());
    }
}
