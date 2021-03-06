package crawler.service;

import java.util.List;

import crawler.entity.Novel;

/**
 * 小説の情報を管理する.
 */
public interface NovelManager {

    /**
     * 小説を登録、既に登録済みであれば更新する.
     *
     * @param url
     *            小説のURL
     */
    void save(String url);

    /**
     * 小説を削除する.
     *
     * @param url
     *            小説のURL
     */
    void delete(String url);

    /**
     * 更新確認対象の小説一覧のIDを取得する.
     *
     * @return 更新確認対象の小説一覧
     */
    List<Long> getCheckTargetId();

    /**
     * 小説の更新を確認し、更新があった場合は内容を保存する.
     *
     * @param checkTargetId
     *            小説の情報のID
     */
    void checkForUpdatesAndSaveHistory(Long checkTargetId);

    /**
     * 小説の更新を確認し、更新があった場合は内容を保存する.
     *
     * @param novel
     *            小説の情報
     */
    void checkForUpdatesAndSaveHistory(Novel novel);
}
