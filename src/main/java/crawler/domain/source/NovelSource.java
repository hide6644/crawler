package crawler.domain.source;

import java.util.Date;
import java.util.List;

import crawler.domain.Novel;
import crawler.domain.NovelHistory;
import crawler.util.NovelElementsUtil;
import crawler.util.NovelElementsUtil.ContensType;
import crawler.util.NovelManagerUtil;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

/**
 * 小説のhtml
 */
public class NovelSource {

    /** 小説のURL */
    private String url;

    /** 小説のhtml */
    private Source html;

    /** 小説の情報 */
    private Novel novel;

    /** 小説の更新履歴 */
    private NovelHistory novelHistory;

    /**
     * コンストラクタ.
     *
     * @param url
     *            小説のURL
     */
    public NovelSource(String url) {
        // URLからhtmlを取得
        this(url, NovelManagerUtil.getSource(NovelManagerUtil.getUrl(url)));
    }

    /**
     * コンストラクタ.
     *
     * @param url
     *            小説のURL
     * @param html
     *            小説のhtml
     */
    public NovelSource(String url, Source html) {
        if (url == null || html == null) {
            throw new NullPointerException();
        }
        this.url = url;
        this.html = html;
    }

    /**
     * 小説のhtmlを小説の情報(Novel)に変換する.
     */
    public void mapping() {
        if (novel == null) {
            novel = new Novel();
        } else {
            // 更新の場合、Historyを作成
            novel.setUpdateDate(new Date());

            // 小説の更新履歴を作成
            if (!novel.getTitle().equals(NovelElementsUtil.getTitle(html))) {
                if (novelHistory == null) {
                    novelHistory = new NovelHistory();
                }
                // タイトルに差異がある場合
                novelHistory.setTitle(novel.getTitle());
            }
            if (!novel.getWritername().equals(NovelElementsUtil.getWritername(html))) {
                if (novelHistory == null) {
                    novelHistory = new NovelHistory();
                }
                // 作者名に差異がある場合
                novelHistory.setWritername(novel.getWritername());
            }
            if (!novel.getDescription().equals(NovelElementsUtil.getDescription(html))) {
                if (novelHistory == null) {
                    novelHistory = new NovelHistory();
                }
                // 解説に差異がある場合
                novelHistory.setDescription(novel.getDescription());
            }
            if (!novel.getBody().equals(NovelElementsUtil.getBody(html))) {
                if (novelHistory == null) {
                    novelHistory = new NovelHistory();
                }
                // 本文に差異がある場合
                novelHistory.setBody(novel.getBody());
            }

            if (novelHistory != null) {
                novelHistory.setNovel(novel);
                novel.addNovelHistory(novelHistory);
            }
        }

        // 小説の情報を取得
        novel.setTitle(NovelElementsUtil.getTitle(html));
        novel.setWritername(NovelElementsUtil.getWritername(html));
        novel.setDescription(NovelElementsUtil.getDescription(html));
        novel.setBody(NovelElementsUtil.getBody(html));
        novel.setUrl(url);
        novel.setDeleted(false);
    }

    /**
     * 小説の本文から小説の章のリストを取得する.
     *
     * @return 小説の章のリスト
     */
    public List<Element> getChapterElementList() {
        return new Source(novel.getBody()).getAllElements("dl");
    }

    /**
     * 小説の本文の履歴から小説の章のリストを取得する.
     *
     * @return 小説の章のリスト
     */
    public List<Element> getChapterHistoryElementList() {
        List<Element> chapterHistoryList = null;

        if (novelHistory != null) {
            Source novelHistoryBodyHtml = new Source(novelHistory.getBody());
            chapterHistoryList = novelHistoryBodyHtml.getAllElements("dl");

            if (chapterHistoryList.size() == 0) {
                chapterHistoryList = novelHistoryBodyHtml.getAllElements("tr");
            }
        }

        return chapterHistoryList;
    }

    /**
     * 小説の本文から小説の章のhtml要素の種類を取得する.
     *
     * @return 小説の章のhtml要素の種類
     */
    public ContensType getChapterHistoryElementContensType() {
        List<Element> chapterHistoryElementList = getChapterHistoryElementList();

        if (chapterHistoryElementList.get(0).getAllElementsByClass("period_subtitle").size() > 0) {
            return NovelElementsUtil.ContensType.PERIOD_SUBTITLE;
        } else if (chapterHistoryElementList.get(0).getAllElementsByClass("long_subtitle").size() > 0) {
            return NovelElementsUtil.ContensType.LONG_SUBTITLE;
        } else {
            return NovelElementsUtil.ContensType.SUBTITLE;
        }
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

    public Novel getNovel() {
        return novel;
    }

    public void setNovel(Novel novel) {
        this.novel = novel;
    }

    public NovelHistory getNovelHistory() {
        return novelHistory;
    }

    public void setNovelHistory(NovelHistory novelHistory) {
        this.novelHistory = novelHistory;
    }
}
