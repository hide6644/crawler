package crawler.domain.source;

import java.time.LocalDateTime;

import crawler.domain.NovelChapter;
import crawler.domain.NovelChapterHistory;
import crawler.exception.NovelNotFoundException;
import crawler.util.NovelElementsUtil;
import crawler.util.NovelManagerUtil;

/**
 * 小説の章の情報のhtmlを保持するクラス.
 */
public class NovelChapterSource extends BaseSource {

    /** 小説の章の情報 */
    private NovelChapter novelChapter;

    /** 小説の章の更新履歴 */
    private NovelChapterHistory novelChapterHistory;

    /**
     * コンストラクタ.
     *
     * @param url
     *            小説の章のURL
     * @throws NovelNotFoundException
     *             小説の章が見つからない
     */
    protected NovelChapterSource(String url) throws NovelNotFoundException {
        this.url = NovelManagerUtil.getUrl(url);
        // URLからhtmlを取得
        html = NovelManagerUtil.getSource(this.url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void mapping() {
        if (novelChapter == null) {
            add = true;
            novelChapter = new NovelChapter();
        } else {
            // 更新の場合、Historyを作成
            add = false;

            // 小説の章の更新履歴を作成
            checkTitleDiff();
            checkBodyDiff();

            if (novelChapterHistory != null) {
                // 小説の章の更新履歴が作成された場合
                novelChapterHistory.setNovelChapter(novelChapter);
                novelChapter.addNovelChapterHistory(novelChapterHistory);
            }

            // 更新日時を変更
            novelChapter.setUpdateDate(LocalDateTime.now());
        }

        // 小説の章の情報を変更
        novelChapter.setTitle(NovelElementsUtil.getChapterTitle(html));
        novelChapter.setUrl(url.toString());
        novelChapter.setBody(NovelElementsUtil.getChapterBody(html));
    }

    /**
     * タイトルに差異があるか確認し、差異があれば小説の章の更新履歴を作成する.
     */
    void checkTitleDiff() {
        if (!novelChapter.getTitle().equals(NovelElementsUtil.getChapterTitle(html))) {
            createNovelChapterHistory().setTitle(novelChapter.getTitle());
        }
    }

    /**
     * 本文に差異があるか確認し、差異があれば小説の更新履歴を作成する.
     */
    void checkBodyDiff() {
        // 本文は常に変更ありとする
        createNovelChapterHistory().setBody(novelChapter.getBody());
    }

    /**
     * NovelChapterSourceのインスタンスを生成する.
     *
     * @param url
     *            URL
     * @param novelChapter
     *            小説の章の情報
     * @return NovelChapterSourceのインスタンス
     * @throws NovelNotFoundException
     *             指定されたURLが取得出来ない
     */
    public static NovelChapterSource newInstance(String url, NovelChapter novelChapter) throws NovelNotFoundException {
        NovelChapterSource novelChapterSource = new NovelChapterSource(url);
        novelChapterSource.setNovelChapter(novelChapter);
        novelChapterSource.mapping();

        return novelChapterSource;
    }

    public NovelChapter getNovelChapter() {
        return novelChapter;
    }

    public void setNovelChapter(NovelChapter novelChapter) {
        this.novelChapter = novelChapter;
    }

    public NovelChapterHistory getNovelChapterHistory() {
        return novelChapterHistory;
    }

    public void setNovelChapterHistory(NovelChapterHistory novelChapterHistory) {
        this.novelChapterHistory = novelChapterHistory;
    }

    /**
     * 小説の章の更新履歴を作成する.
     *
     * @return 小説の章の更新履歴
     */
    public NovelChapterHistory createNovelChapterHistory() {
        if (novelChapterHistory == null) {
            novelChapterHistory = new NovelChapterHistory();
        }

        return novelChapterHistory;
    }
}
