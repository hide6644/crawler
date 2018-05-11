package crawler.service;

import crawler.domain.source.NovelSource;
import crawler.exception.NovelNotFoundException;

/**
 * 小説の付随情報を管理する.
 */
public interface NovelInfoManager {

    /**
     * 小説の付随情報を設定する.
     *
     * @param novelSource
     *            小説の情報
     * @throws NovelNotFoundException
     *             小説の情報が見つからない
     */
    public void saveNovelInfo(NovelSource novelSource) throws NovelNotFoundException;
}
