package crawler.dao;

import java.util.List;
import java.util.Map;

import crawler.entity.Novel;

/**
 * NovelのHibernate Search DAOのインターフェース.
 */
public interface NovelSearch extends HibernateSearch {

    /**
     * 全文検索する.
     *
     * @param searchTerm
     *            検索文字列
     * @return 検索結果のリスト
     */
    List<Novel> search(String searchTerm);

    /**
     * ファセットを作成する.
     *
     * @param field
     *            対象となる項目
     * @param maxCount
     *            ファセットの最大件数
     * @return ファセットのマップ
     */
    Map<String, Long> facet(String field, int maxCount);
}
