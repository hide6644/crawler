package crawler.domain.source;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
    private URL url;

    /** 小説のhtml */
    private Source html;

    /** 小説の情報 */
    private Novel novel;

    /** 小説の更新履歴 */
    private NovelHistory novelHistory;

    /** 新規フラグ */
    private boolean add;

    /**
     * コンストラクタ.
     *
     * @param url
     *            小説のURL
     */
    public NovelSource(String url) {
        this.url = NovelManagerUtil.getUrl(url);
        // URLからhtmlを取得
        html = NovelManagerUtil.getSource(this.url);

        if (html == null) {
            throw new NullPointerException();
        }
    }

    /**
     * コンストラクタ.
     *
     * @param url
     *            小説のURL
     * @param html
     *            小説のhtml
     */
    public NovelSource(URL url, Source html) {
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
            add = true;
            novel = new Novel();
        } else {
            add = false;
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
        novel.setUrl(url.toString());
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
     * 小説の本文の履歴から小説の章のセットを取得する.
     *
     * @return 小説の章のセット
     */
    public Set<NovelBodyElement> getChapterHistoryElementSet() {
        if (novelHistory != null) {
            Source novelHistoryBodyHtml = new Source(novelHistory.getBody());
            List<Element> chapterHistoryElementList = novelHistoryBodyHtml.getAllElements("dl");

            if (chapterHistoryElementList.size() == 0) {
                // 古いスタイルの場合
                chapterHistoryElementList = novelHistoryBodyHtml.getAllElements("tr");
            }

            return chapterHistoryElementList.stream()
                    .filter(chapterElement -> NovelElementsUtil.existsChapterLink(chapterElement))
                    .map(chapterElement -> new NovelBodyElement(chapterElement)).collect(Collectors.toSet());
        } else {
            return null;
        }
    }

    /**
     * 小説の情報から小説の付随情報のリンクを取得する.
     *
     * @return 小説の付随情報のリンク
     */
    public String getNovelInfoLink() {
        return html.getElementById("novel_header").getAllElements("a").stream()
                .filter(linkElement -> linkElement.getTextExtractor().toString().equals("小説情報"))
                .map(linkElement -> linkElement.getAttributeValue("href"))
                .findFirst().orElse(null);
    }

    /**
     * 小説のUrlのホスト部分まで取得する.
     *
     * @return 小説のUrlのホスト部分
     */
    public String getHostUrl() {
        return url.getProtocol() + "://" + url.getHost();
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
