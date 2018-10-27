package crawler.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import crawler.entity.Novel;

/**
 * 小説のDAOのインターフェイス.
 */
public interface NovelDao extends JpaRepository<Novel, Long> {

    /**
     * 指定の「URL」の小説を取得する.
     *
     * @param url
     *            URL
     * @return 小説
     */
    Novel findByUrl(String url);

    /**
     * 指定の「最終確認日時」以前の小説の一覧を取得する.
     *
     * @param checkedDate
     *            最終確認日時
     * @return 小説の一覧
     */
    List<Novel> findByDeletedFalseAndCheckedDateLessThanEqualAndCheckEnableTrue(@Param("checkedDate") LocalDateTime checkedDate);

    /**
     * 未読小説の一覧を取得する.
     *
     * @return 小説の一覧
     */
    List<Novel> findByUnreadTrueOrderByTitleAndNovelChapterId();

    /**
     * 小説の最終更新日時一覧を取得する.
     *
     * @return 小説の一覧
     */
    List<Novel> findByDeletedFalseOrderByTitle();
}
