package crawler.service;

import crawler.exception.NovelConnectException;
import crawler.exception.NovelNotFoundException;
import crawler.mapping.yomou.syosetu.com.NovelSource;

/**
 * 小説の付随情報を管理する.
 */
public interface NovelInfoManager {

    /**
     * 小説の付随情報を設定する.
     *
     * @param novelSource
     *            小説の情報
     * @throws NovelConnectException
     *             URLに繋がらない
     * @throws NovelNotFoundException
     *             URLで指定されたコンテンツが見つからない
     */
    void saveNovelInfo(NovelSource novelSource) throws NovelConnectException, NovelNotFoundException;
}
