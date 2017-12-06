package crawler.service;

import crawler.domain.NovelChapter;
import crawler.domain.source.NovelSource;

/**
 * 小説の章を管理する.
 */
public interface NovelChapterManager extends GenericManager<NovelChapter, Long> {

    /**
     * 全ての小説の章を設定する.
     *
     * @param novelSource
     *            小説の情報
     */
    public void saveAllNovelChapter(NovelSource novelSource);
}
