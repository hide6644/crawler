package crawler.service;

import java.util.List;

import crawler.domain.Novel;

/**
 * 小説の情報を管理する.
 */
public interface NovelManager extends GenericManager<Novel, Long> {

    /**
     * 未読小説を取得する.
     *
     * @return 未読小説リスト
     */
    List<Novel> getNovelsByUnread();

    /**
     * 小説を保存する.
     *
     * @param url
     *            小説のURL
     */
    void save(String url);

    /**
     * 小説の更新を確認する.
     */
    void checkUpdate();

    /**
     * 未読小説の一覧を作成し、メールで送信する.
     */
    void sendReport();
}
