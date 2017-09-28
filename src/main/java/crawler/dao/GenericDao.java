package crawler.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.search.query.facet.Facet;

/**
 * 一般的なCRUD DAOのインターフェース.
 */
public interface GenericDao<T, K extends Serializable> {

    /**
     * すべてのオブジェクトを取得する.
     *
     * @return オブジェクト一覧
     */
    List<T> getAll();

    /**
     * 重複を除くすべてのオブジェクトを取得する.
     *
     * @return 重複を除くオブジェクト一覧
     */
    List<T> getAllDistinct();

    /**
     * 指定されたキーのオブジェクトを取得する.
     *
     * @param id
     *            主キー項目
     * @return オブジェクト
     */
    T get(K id);

    /**
     * 指定されたキーのオブジェクトが存在するか.
     *
     * @param id
     *            主キー項目
     * @return true:存在する、false:存在しない
     */
    boolean exists(K id);

    /**
     * 指定されたオブジェクトを永続化する.
     *
     * @param object
     *            永続化するオブジェクト
     * @return 永続化されたオブジェクト
     */
    T save(T object);

    /**
     * 指定されたオブジェクトを削除する.
     *
     * @param object
     *            削除するオブジェクト
     */
    void remove(T object);

    /**
     * 指定されたキーのオブジェクトを削除する.
     *
     * @param id
     *            主キー項目
     */
    void remove(K id);

    /**
     * 指定されたクエリを使用し検索する.
     *
     * @param queryName
     *            クエリの名称
     * @param queryParams
     *            クエリのパラメータ
     * @return 検索結果のオブジェクトのリスト
     */
    List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams);

    /**
     * 全文検索する.
     *
     * @param searchTerm
     *            検索文字列
     * @param searchField
     *            検索項目
     * @return 検索結果のオブジェクトのリスト
     */
    List<T> search(String[] searchTerm, String[] searchField);

    /**
     * 全文検索する.
     *
     * @param searchTerm
     *            検索文字列
     * @return 検索結果のオブジェクトのリスト
     */
    List<T> search(String searchTerm);

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
