package crawler.domain.source;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import crawler.domain.Novel;
import crawler.domain.NovelHistory;
import crawler.exception.NovelNotFoundException;
import crawler.util.NovelElementsUtil;
import crawler.util.NovelManagerUtil;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

/**
 * 小説のhtmlを保持するクラス.
 */
public class NovelSource extends BaseSource {

    /** 小説の情報 */
    private Novel novel;

    /** 小説の更新履歴 */
    private NovelHistory novelHistory;

    /**
     * コンストラクタ.
     *
     * @param url
     *            小説のURL
     * @throws NovelNotFoundException
     *             小説が見つからない
     */
    public NovelSource(String url) throws NovelNotFoundException {
        this.url = NovelManagerUtil.getUrl(url);
        // URLからhtmlを取得
        html = NovelManagerUtil.getSource(this.url);
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
     * 小説の本文から小説の目次リストを取得する.
     *
     * @return 小説の目次リスト
     */
    public List<NovelIndexElement> getNovelIndexList() {
        return new Source(novel.getBody()).getAllElements("dl").stream()
                .filter(chapterElement -> NovelElementsUtil.existsChapterLink(chapterElement))
                .map(chapterElement -> new NovelIndexElement(chapterElement)).collect(Collectors.toList());
    }

    /**
     * 小説の本文の履歴から小説の目次セットを取得する.
     *
     * @return 小説の目次セット
     */
    public Set<NovelIndexElement> getNovelHistoryIndexSet() {
        if (novelHistory != null) {
            Source novelHistoryBodyHtml = new Source(novelHistory.getBody());
            List<Element> chapterHistoryElementList = novelHistoryBodyHtml.getAllElements("dl");

            if (chapterHistoryElementList.size() == 0) {
                // 古いスタイルの場合
                chapterHistoryElementList = novelHistoryBodyHtml.getAllElements("tr");
            }

            return chapterHistoryElementList.stream()
                    .filter(chapterElement -> NovelElementsUtil.existsChapterLink(chapterElement))
                    .map(chapterElement -> new NovelIndexElement(chapterElement)).collect(Collectors.toSet());
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
