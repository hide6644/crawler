package crawler.dao;

import java.util.List;

import crawler.domain.NovelInfo;

/**
 * 小説付随情報のDAOのインターフェイス.
 */
public interface NovelInfoDao extends GenericDao<NovelInfo, Long> {

    /**
     * 指定のキーワードで小説の付随情報を全文検索する.
     *
     * @param keyword
     *            キーワード
     * @return 小説の付随情報の一覧
     */
    List<NovelInfo> searchNovelInfoByKeyword(String keyword);
}
