package crawler.domain.source;

import java.net.URL;
import java.util.Date;

import crawler.domain.NovelChapter;
import crawler.domain.NovelChapterHistory;
import crawler.exception.NovelNotFoundException;
import crawler.util.NovelElementsUtil;
import crawler.util.NovelManagerUtil;
import net.htmlparser.jericho.Source;

/**
 * 小説の章の情報のhtml
 */
public class NovelChapterSource {

    /** 小説の章のURL */
    private URL url;

    /** 小説の章のhtml */
    private Source html;

    /** 小説の章の情報 */
    private NovelChapter novelChapter;

    /** 小説の章の更新履歴 */
    private NovelChapterHistory novelChapterHistory;

    /** 新規フラグ */
    private boolean add;

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

    /**
     * 新規か、更新か.
     *
     * @return true:新規、false:更新
     */
    public boolean isAdd() {
        return add;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Source getHtml() {
        return html;
    }

    public void setHtml(Source html) {
        this.html = html;
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
