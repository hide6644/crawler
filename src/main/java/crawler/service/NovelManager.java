package crawler.service;

import java.util.List;

import crawler.domain.Novel;

/**
 * 小説の情報を管理する.
 */
public interface NovelManager extends GenericManager<Novel, Long> {

    /**
     * 小説を新規に保存する.
     *
     * @param url
     *            小説のURL
     */
    void add(String url);

    /**
     * 更新確認対象の小説一覧のIDを取得する.
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

    /**
     * 未読小説の一覧を取得する.
     *
     * @return 未読小説の一覧
     */
    List<Novel> getUnreadNovels();

    /**
     * 未読小説の一覧をメールで送信する.
     */
    void sendReport();
}
