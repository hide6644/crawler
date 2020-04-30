package crawler.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import crawler.entity.NovelHistory;

/**
 * 小説の履歴のDAOのインターフェイス.
 */
public interface NovelHistoryDao extends JpaRepository<NovelHistory, Long> {
}
