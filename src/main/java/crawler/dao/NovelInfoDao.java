package crawler.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import crawler.entity.NovelInfo;

/**
 * 小説付随情報のDAOのインターフェイス.
 */
public interface NovelInfoDao extends JpaRepository<NovelInfo, Long> {
}
