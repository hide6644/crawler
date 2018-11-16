package crawler.dao;

import java.util.List;
import java.util.stream.Stream;

import org.apache.lucene.search.BooleanClause.Occur;
import org.hibernate.search.query.facet.Facet;

/**
 * Hibernate Search DAOのインターフェース.
 */
public interface HibernateSearch<T> {

    /**
     * 全文検索する.
     *
     * @param searchTerms
     *            検索文字列
     * @param searchFields
     *            検索項目
     * @param searchFlags
     *            検索項目のフラグ
     * @return 検索結果のオブジェクトのリスト
     */
    Stream<T> search(String[] searchTerms, String[] searchFields, Occur[] searchFlags);

    /**
     * 全文検索する.
     *
     * @param searchTerms
     *            検索文字列
     * @param searchFields
     *            検索項目
     * @return 検索結果のオブジェクトのリスト
     */
    Stream<T> search(String[] searchTerms, String[] searchFields);

    /**
     * 全文検索する.
     *
     * @param searchTerm
     *            検索文字列
     * @return 検索結果のオブジェクトのリスト
     */
    Stream<T> search(String searchTerm);

    /**
     * 指定の範囲のオブジェクトを取得(全文検索)する.
     *
     * @param searchTerms
     *            検索文字列
     * @param searchFields
     *            検索項目
     * @param offset
     *            開始位置
     * @param limit
     *            取得数
     * @return 検索結果のオブジェクトのリスト
     */
    public Stream<T> search(String[] searchTerms, String[]searchFields ,Integer offset, Integer limit) ;

    /**
     * 指定の範囲のオブジェクトを取得(全文検索)する.
     *
     * @param searchTerm
     *            検索文字列
     * @param offset
     *            開始位置
     * @param limit
     *            取得数
     * @return 検索結果のオブジェクトのリスト
     */
    public Stream<T> search(String searchTerm, Integer offset, Integer limit) ;

    /**
     * 件数を取得(全文検索)する.
     *
     * @param searchTerms
     *            検索文字列
     * @param searchFields
     *            検索項目
     * @return 件数
     */
    public long count(String[] searchTerms, String[]searchFields);

    /**
     * 件数を取得(全文検索)する.
     *
     * @param searchTerm
     *            検索文字列
     * @return 件数
     */
    public long count(String searchTerm);

    /**
     * ファセットを作成する.
     *
     * @param field
     *            対象となる項目
     * @param maxCount
     *            ファセットの最大件数
     * @return ファセットのリスト
     */
    List<Facet> facet(String field, int maxCount);

    /**
     * インデックスを再作成する.
     */
    void reindex();

    /**
     * 全てのインデックスを再作成する.
     *
     * @param async
     *            true:非同期、false:同期
     */
    void reindexAll(boolean async);
}
