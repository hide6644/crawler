package crawler.service;

import crawler.domain.source.NovelChapterSource;
import crawler.domain.source.NovelIndexElement;

/**
 * 小説の章の付随情報を管理する.
 */
public interface NovelChapterInfoManager {

    /**
     * 小説の章の付随情報を設定する.
     *
     * @param novelIndexElement
     *            小説の目次のhtml element
     * @param novelChapterSource
     *            小説の章の情報のhtml
     */
    public void saveNovelChapterInfo(NovelIndexElement novelIndexElement, NovelChapterSource novelChapterSource);
}
