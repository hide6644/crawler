package crawler.service;

import crawler.dto.NovelChapterSource;
import crawler.dto.NovelIndexElement;

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
    void saveNovelChapterInfo(NovelIndexElement novelIndexElement, NovelChapterSource novelChapterSource);
}
