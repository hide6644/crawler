package crawler.service;

import java.util.stream.Stream;

import crawler.entity.Novel;

/**
 * 小説の情報の出力を管理する.
 */
public interface NovelOutputManager {

    /**
     * 未読小説の一覧を取得する.
     *
     * @return 未読小説の一覧
     */
    Stream<Novel> getUnreadNovels();

    /**
     * 小説の最終更新日時一覧を取得する.
     *
     * @return 小説の最終更新日時一覧
     */
    Stream<Novel> getModifiedDateOfNovels();

    /**
     * 未読小説の一覧をメールで送信する.
     */
    void sendUnreadReport();

    /**
     * 小説の最終更新日時一覧をメールで送信する.
     */
    void sendModifiedDateReport();
}
