package crawler.service;

import java.util.List;

import crawler.entity.Novel;

/**
 * 小説の情報を検索する.
 */
public interface NovelSearchManager {

    /**
     * 全文検索する.
     *
     * @param searchTerm
     *            検索文字列
     * @return 検索結果のリスト
     */
    List<Novel> search(String searchTerm);

    /**
     * 全てのインデックスを再作成する.
     *
     * @param async
     *            true:非同期、false:同期
     */
    void reindexAll(boolean async);
}
