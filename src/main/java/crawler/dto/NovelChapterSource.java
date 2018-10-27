package crawler.dto;

import java.time.LocalDateTime;

import crawler.entity.NovelChapter;
import crawler.entity.NovelChapterHistory;
import crawler.exception.NovelNotFoundException;
import crawler.util.NovelElementsUtil;

/**
 * 小説の章の情報のhtmlを保持するクラス.
 */
public class NovelChapterSource extends BaseSource {

    /** 小説の章の情報 */
    private final NovelChapter novelChapter;

    /** 小説の章の更新履歴 */
    private NovelChapterHistory novelChapterHistory;

    /**
     * コンストラクタ.
     *
     * @param url
     *            小説の章のURL
     * @param add
     *            true:新規、false:更新
     * @param novelChapter
     *            小説の章の付随情報
     * @throws NovelNotFoundException
     *             小説の章が見つからない
     */
    private NovelChapterSource(final String url, final boolean add, final NovelChapter novelChapter) throws NovelNotFoundException {
        super(url, add);
        this.novelChapter = novelChapter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NovelChapterSource mapping() {
        if (!add) {
            // 更新の場合、Historyを作成
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

        return this;
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
    public static NovelChapterSource newInstance(final String url, final NovelChapter novelChapter) throws NovelNotFoundException {
        if (novelChapter == null) {
            return new NovelChapterSource(url, true, new NovelChapter()).mapping();
        } else {
            return new NovelChapterSource(url, false, novelChapter).mapping();
        }
    }

    public NovelChapter getNovelChapter() {
        return novelChapter;
    }

    public NovelChapterHistory getNovelChapterHistory() {
        return novelChapterHistory;
    }

    /**
     * 小説の章の更新履歴を作成する.
     *
     * @return 小説の章の更新履歴
     */
    private NovelChapterHistory createNovelChapterHistory() {
        if (novelChapterHistory == null) {
            novelChapterHistory = new NovelChapterHistory();
        }

        return novelChapterHistory;
    }
}
