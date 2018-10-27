package crawler.service;

import crawler.dto.NovelSource;

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
    public void saveAllNovelChapter(NovelSource novelSource);
}
