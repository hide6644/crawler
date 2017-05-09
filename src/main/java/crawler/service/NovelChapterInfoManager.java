package crawler.service;

import crawler.domain.NovelChapterInfo;
import crawler.domain.source.NovelChapterSource;
import net.htmlparser.jericho.Element;

/**
 * 小説の章の付随情報を管理する.
 */
public interface NovelChapterInfoManager extends GenericManager<NovelChapterInfo, Long> {

    /**
     * 小説の章の付随情報を設定する.
     *
     * @param chapterElement
     *            小説の本文のhtml element要素
     * @param novelChapterSource
     *            小説の章の情報
     */
    public void saveNovelChapterInfo(Element chapterElement, NovelChapterSource novelChapterSource);
}
