package crawler.service;

import crawler.mapping.NovelSource;

/**
 * 小説の章を管理する.
 */
public interface NovelChapterManager {

    /**
     * 全ての小説の章を設定する.
     *
     * @param novelSource
     *            小説の情報
     */
    void saveAllNovelChapter(NovelSource novelSource);
}
