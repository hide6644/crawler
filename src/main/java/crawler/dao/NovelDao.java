package crawler.dao;

import java.util.Date;
import java.util.List;

import crawler.domain.Novel;

/**
 * 小説のDAOのインターフェイス.
 */
public interface NovelDao extends GenericDao<Novel, Long> {

    /**
     * 指定の「URL」の小説を取得する.
     *
     * @param url
     *            URL
     * @return 小説
     */
    Novel getByUrl(String url);

    /**
     * 指定の「最終確認日時」以前の小説の一覧を取得する.
     *
     * @param checkedDate
     *            最終確認日時
     * @return 小説の一覧
     */
    List<Novel> getByCheckedDateLessThanEqualAndCheckEnableTrue(Date checkedDate);

    /**
     * 未読小説の一覧を取得する.
     *
     * @return 小説の一覧
     */
    List<Novel> getByUnreadTrueOrderByTitleAndNovelChapterId();

    /**
     * 小説の最終更新日時一覧を取得する.
     *
     * @return 小説の一覧
     */
    List<Novel> getOrderByTitle();
}
