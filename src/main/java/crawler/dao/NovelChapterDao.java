package crawler.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import crawler.entity.NovelChapter;

/**
 * 小説の章のDAOのインターフェイス.
 */
public interface NovelChapterDao extends JpaRepository<NovelChapter, Long> {

    /**
     * 小説の章を取得する.
     *
     * @param url
     *            URL
     * @return 小説の章
     */
    NovelChapter findByUrl(String url);
}
