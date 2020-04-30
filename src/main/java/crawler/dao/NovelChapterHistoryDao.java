package crawler.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import crawler.entity.NovelChapterHistory;

/**
 * 小説の章の履歴のDAOのインターフェイス.
 */
public interface NovelChapterHistoryDao extends JpaRepository<NovelChapterHistory, Long> {
}
