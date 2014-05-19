package crawler.service;

import crawler.domain.Novel;

/**
 * 小説を管理する.
 */
public interface NovelManager extends GenericManager<Novel, Long> {

    /**
     * 未読小説の一覧を作成し、メールで送信する.
     */
    void getReport();

    /**
     * 小説を追加する.
     *
     * @param url
     *            小説のURL
     */
    void create(String url);

    /**
     * 小説の更新を確認する.
     */
    void update();
}
