package crawler.mapping.yomou.syosetu.com;

import java.net.URL;
import java.time.LocalDateTime;

import crawler.entity.NovelChapter;
import crawler.entity.NovelChapterHistory;
import crawler.exception.NovelConnectException;
import crawler.exception.NovelNotFoundException;
import crawler.util.NovelManagerUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 小説の章の情報のhtmlを保持するクラス.
 */
@Setter
@Getter
public class NovelChapterSource extends BaseSource {

    /** 小説の章の情報 */
    private final NovelChapter novelChapter;

    /** 小説の章の更新履歴 */
    private NovelChapterHistory novelChapterHistory;

    /** 小説の章のURL */
    private final URL url;

    /**
     * コンストラクタ.
     *
     * @param add
     *            true:新規、false:更新
     * @param url
     *            小説の章のURL
     * @param novelChapter
     *            小説の章の付随情報
     * @throws NovelConnectException
     *             URLに繋がらない
     * @throws NovelNotFoundException
     *             URLで指定されたコンテンツが見つからない
     */
    private NovelChapterSource(final boolean add, final String url, final NovelChapter novelChapter) throws NovelConnectException, NovelNotFoundException {
        super(url, add);
        this.url = NovelManagerUtil.getUrl(url);
        this.novelChapter = novelChapter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NovelChapterSource mapping() {
        if (add) {
            // 小説の章の情報を設定
            novelChapter.setTitle(NovelElementsUtil.getChapterTitle(html));
            novelChapter.setUrl(url.toString());
            novelChapter.setBody(NovelElementsUtil.getChapterBody(html));
        } else {
            // 更新の場合、Historyを作成
            // 小説の章の更新履歴を作成
            checkTitleDiff();
            checkBodyDiff();

            if (novelChapterHistory != null) {
                // 小説の章の更新履歴が作成された場合
                novelChapterHistory.setNovelChapter(novelChapter);
            }

            // 更新日時を変更
            novelChapter.setUpdateDate(LocalDateTime.now());
        }

        return this;
    }

    /**
     * タイトルに差異があるか確認し、差異があれば小説の章の更新履歴を作成する.
     */
    private void checkTitleDiff() {
        String chapterTitle = NovelElementsUtil.getChapterTitle(html);

        if (!novelChapter.getTitle().equals(chapterTitle)) {
            createNovelChapterHistory().setTitle(novelChapter.getTitle());
            novelChapter.setTitle(chapterTitle);
        }
    }

    /**
     * 本文に差異があるか確認し、差異があれば小説の更新履歴を作成する.
     */
    private void checkBodyDiff() {
        // 本文は常に変更ありとする
        createNovelChapterHistory().setBody(novelChapter.getBody());
        novelChapter.setBody(NovelElementsUtil.getChapterBody(html));
    }

    /**
     * NovelChapterSourceのインスタンスを生成する.
     *
     * @param url
     *            小説の章のURL
     * @param novelChapter
     *            小説の章の情報
     * @return NovelChapterSourceのインスタンス
     * @throws NovelConnectException
     *             URLに繋がらない
     * @throws NovelNotFoundException
     *             URLで指定されたコンテンツが見つからない
     */
    public static NovelChapterSource newInstance(final String url, final NovelChapter novelChapter) throws NovelConnectException, NovelNotFoundException {
        if (novelChapter == null) {
            return new NovelChapterSource(true, url, new NovelChapter()).mapping();
        } else {
            return new NovelChapterSource(false, url,  novelChapter).mapping();
        }
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
