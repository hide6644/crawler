package crawler.domain.source;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import crawler.domain.Novel;
import crawler.domain.NovelHistory;
import crawler.util.NovelElementsUtil;
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
    public List<NovelBodyElement> getChapterElementList() {
        return new Source(novel.getBody()).getAllElements("dl").stream()
                .filter(chapterElement -> NovelElementsUtil.existsChapterLink(chapterElement))
                .map(chapterElement -> new NovelBodyElement(chapterElement)).collect(Collectors.toList());
    }

    /**
     * 小説の本文の履歴から小説の章のリストを取得する.
     *
     * @return 小説の章のリスト
     */
    public List<NovelBodyElement> getChapterHistoryElementList() {
        List<Element> chapterHistoryElementList = null;

        if (novelHistory != null) {
            Source novelHistoryBodyHtml = new Source(novelHistory.getBody());
            chapterHistoryElementList = novelHistoryBodyHtml.getAllElements("dl");

            if (chapterHistoryElementList.size() == 0) {
                chapterHistoryElementList = novelHistoryBodyHtml.getAllElements("tr");
            }

            return chapterHistoryElementList.stream()
                    .filter(chapterElement -> NovelElementsUtil.existsChapterLink(chapterElement))
                    .map(chapterElement -> new NovelBodyElement(chapterElement)).collect(Collectors.toList());
        } else {
            return null;
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
