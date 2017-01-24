package crawler.domain.source;

import java.util.Date;

import crawler.domain.NovelChapter;
import crawler.domain.NovelChapterHistory;
import crawler.util.NovelElementsUtil;
import crawler.util.NovelManagerUtil;
import net.htmlparser.jericho.Source;

/**
 * 小説の章の情報のhtml
 */
public class NovelChapterSource {

    /** 小説の章のURL */
    private String url;

    /** 小説の章のhtml */
    private Source html;

    /** 小説の章の情報 */
    private NovelChapter novelChapter;

    /** 小説の章の更新履歴 */
    private NovelChapterHistory novelChapterHistory;

    /**
     * コンストラクタ.
     *
     * @param url
     *            小説の章のURL
     */
    public NovelChapterSource(String url) {
        this(url, NovelManagerUtil.getSource(NovelManagerUtil.getUrl(url)));
    }

    /**
     * コンストラクタ.
     *
     * @param url
     *            小説の章のURL
     * @param html
     *            小説の章のhtml
     */
    public NovelChapterSource(String url, Source html) {
        if (url == null || html == null || !NovelElementsUtil.existsChapter(html)) {
            throw new NullPointerException();
        }
        this.url = url;
        this.html = html;
    }

    /**
     * 小説の章のhtmlを小説の章の情報(NovelChapter)に変換する.
     */
    public void mapping() {
        if (novelChapter == null) {
            novelChapter = new NovelChapter();
        } else {
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
        novelChapter.setUrl(url);
        novelChapter.setBody(NovelElementsUtil.getChapterBody(html));
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
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
