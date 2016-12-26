package crawler.service;

import crawler.domain.Novel;
import crawler.domain.NovelChapter;
import net.htmlparser.jericho.Source;

/**
 * 小説の章を管理する.
 */
public interface NovelChapterManager extends GenericManager<NovelChapter, Long> {

    /**
     * 小説の章を設定する.
     *
     * @param novelBodyHtml
     *            小説の本文のhtml要素
     * @param novelHistoryBodyHtml
     *            小説の更新履歴の本文のhtml要素
     * @param savedNovel
     *            小説の情報
     */
    public void saveNovelChapter(Source novelBodyHtml, Source novelHistoryBodyHtml, Novel savedNovel);
}
