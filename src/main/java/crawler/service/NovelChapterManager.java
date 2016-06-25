package crawler.service;

import crawler.domain.Novel;
import crawler.domain.NovelChapter;
import crawler.domain.NovelHistory;
import net.htmlparser.jericho.Source;

/**
 * 小説の章を管理する.
 */
public interface NovelChapterManager extends GenericManager<NovelChapter, Long> {

    /**
     * 小説の章を設定する.
     *
     * @param html
     *            html要素
     * @param novel
     *            小説の情報
     */
    public void saveNovelChapter(Source html, Novel novel);

    /**
     * 小説の章を設定する(更新).
     *
     * @param html
     *            html要素
     * @param savedNovel
     *            小説の情報
     * @param novelHistory
     *            小説の更新履歴
     */
    public void saveNovelChapter(Source html, Novel savedNovel, NovelHistory novelHistory);
}
