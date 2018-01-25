package crawler.service;

import java.util.List;

import crawler.domain.Novel;

/**
 * 小説の情報の出力を管理する.
 */
public interface NovelOutputManager extends GenericManager<Novel, Long> {

    /**
     * 未読小説の一覧を取得する.
     *
     * @return 未読小説の一覧
     */
    List<Novel> getUnreadNovels();

    /**
     * 小説の最終更新日時一覧を取得する.
     *
     * @return 小説の最終更新日時一覧
     */
    List<Novel> getModifiedDateOfNovels();

    /**
     * 未読小説の一覧をメールで送信する.
     */
    void sendUnreadReport();

    /**
     * 小説の最終更新日時一覧をメールで送信する.
     */
    void sendModifiedDateReport();
}
