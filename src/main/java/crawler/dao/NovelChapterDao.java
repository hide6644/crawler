package crawler.dao;

import crawler.domain.NovelChapter;

/**
 * 小説の章のDAOのインターフェイス.
 */
public interface NovelChapterDao extends GenericDao<NovelChapter, Long> {

    /**
     * 小説の章を取得する.
     *
     * @param url
     *            URL
     * @return 小説の章
     */
    NovelChapter getNovelChaptersByUrl(String url);
}
