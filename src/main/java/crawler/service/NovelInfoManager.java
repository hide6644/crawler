package crawler.service;

import crawler.domain.NovelInfo;
import crawler.domain.source.NovelSource;

/**
 * 小説の付随情報を管理する.
 */
public interface NovelInfoManager extends GenericManager<NovelInfo, Long> {

    /**
     * 小説の付随情報を設定する.
     *
     * @param novelSource
     *            小説の情報
     */
    public void saveNovelInfo(NovelSource novelSource);
}
