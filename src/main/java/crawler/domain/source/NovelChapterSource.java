package crawler.domain.source;

import java.util.Date;

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
    public NovelChapterSource(String url) throws NovelNotFoundException {
        this.url = NovelManagerUtil.getUrl(url);
        // URLからhtmlを取得
        html = NovelManagerUtil.getSource(this.url);
    }

    /**
     * 小説の章のhtmlを小説の章の情報(NovelChapter)に変換する.
     */
    public void mapping() {
        if (novelChapter == null) {
            add = true;
            novelChapter = new NovelChapter();
        } else {
            add = false;
            // 更新の場合、Historyを作成
            novelChapter.setUpdateDate(new Date());

            // 小説の章の更新履歴を作成
            if (novelChapterHistory == null) {
                novelChapterHistory = new NovelChapterHistory();
            }

            if (!novelChapter.getTitle().equals(NovelElementsUtil.getChapterTitle(html))) {
                // タイトルに差異がある場合
                novelChapterHistory.setTitle(novelChapter.getTitle());
            }

            novelChapterHistory.setBody(novelChapter.getBody());

            novelChapterHistory.setNovelChapter(novelChapter);
            novelChapter.addNovelChapterHistory(novelChapterHistory);
        }

        // 小説の章の情報を取得
        novelChapter.setTitle(NovelElementsUtil.getChapterTitle(html));
        novelChapter.setUrl(url.toString());
        novelChapter.setBody(NovelElementsUtil.getChapterBody(html));
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
}
