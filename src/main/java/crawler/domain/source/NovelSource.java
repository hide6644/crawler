package crawler.domain.source;

import java.util.Collections;
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

    /** 小説のUrlのホスト名 */
    private String hostname;

    /**
     * コンストラクタ.
     *
     * @param url
     *            小説のURL
     * @throws NovelNotFoundException
     *             小説が見つからない
     */
    protected NovelSource(String url) throws NovelNotFoundException {
        this.url = NovelManagerUtil.getUrl(url);
        // URLからhtmlを取得
        html = NovelManagerUtil.getSource(this.url);
        // URLからホスト名を取得
        hostname = this.url.getProtocol() + "://" + this.url.getHost();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void mapping() {
        if (novel == null) {
            add = true;
            novel = new Novel();
        } else {
            // 更新の場合、Historyを作成
            add = false;

            // 差異をチェックし、差異がある場合、小説の更新履歴を作成
            checkTitleDiff();
            checkWriternameDiff();
            checkDescriptionDiff();
            checkBodyDiff();

            if (novelHistory != null) {
                // 小説の更新履歴が作成された場合
                novelHistory.setNovel(novel);
                novel.addNovelHistory(novelHistory);
            }

            // 更新日時を変更
            novel.setUpdateDate(new Date());
        }

        // 小説の情報を変更
        novel.setTitle(NovelElementsUtil.getTitle(html));
        novel.setWritername(NovelElementsUtil.getWritername(html));
        novel.setDescription(NovelElementsUtil.getDescription(html));
        novel.setBody(NovelElementsUtil.getBody(html));
        novel.setUrl(url.toString());
        novel.setDeleted(false);
    }

    /**
     * タイトルに差異があるか確認し、差異があれば小説の更新履歴を作成する.
     */
    void checkTitleDiff() {
        if (!novel.getTitle().equals(NovelElementsUtil.getTitle(html))) {
            createNovelHistory().setTitle(novel.getTitle());
        }
    }

    /**
     * 作者名に差異があるか確認し、差異があれば小説の更新履歴を作成する.
     */
    void checkWriternameDiff() {
        if (!novel.getWritername().equals(NovelElementsUtil.getWritername(html))) {
            createNovelHistory().setWritername(novel.getWritername());
        }
    }

    /**
     * 解説に差異があるか確認し、差異があれば小説の更新履歴を作成する.
     */
    void checkDescriptionDiff() {
        if (!novel.getDescription().equals(NovelElementsUtil.getDescription(html))) {
            createNovelHistory().setDescription(novel.getDescription());
        }
    }

    /**
     * 本文に差異があるか確認し、差異があれば小説の更新履歴を作成する.
     */
    void checkBodyDiff() {
        if (!novel.getBody().equals(NovelElementsUtil.getBody(html))) {
            createNovelHistory().setBody(novel.getBody());
        }
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

            if (chapterHistoryElementList.isEmpty()) {
                // 古いスタイルの場合
                chapterHistoryElementList = novelHistoryBodyHtml.getAllElements("tr");
            }

            return chapterHistoryElementList.stream()
                    .filter(chapterElement -> NovelElementsUtil.existsChapterLink(chapterElement))
                    .map(chapterElement -> new NovelIndexElement(chapterElement)).collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
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
     * 小説のUrlのホスト名を取得する.
     *
     * @return 小説のUrlのホスト名
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * NovelSourceのインスタンスを生成する.
     *
     * @param url
     *            URL
     * @return NovelSourceのインスタンス
     * @throws NovelNotFoundException
     *             指定されたURLが取得出来ない
     */
    public static NovelSource newInstance(String url) throws NovelNotFoundException {
        return newInstance(url, null);
    }

    /**
     * NovelSourceのインスタンスを生成する.
     *
     * @param url
     *            URL
     * @param novel
     *            小説の情報
     * @return NovelSourceのインスタンス
     * @throws NovelNotFoundException
     *             指定されたURLが取得出来ない
     */
    public static NovelSource newInstance(String url, Novel novel) throws NovelNotFoundException {
        NovelSource novelSource = new NovelSource(url);
        novelSource.setNovel(novel);
        novelSource.mapping();

        return novelSource;
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

    /**
     * 小説の更新履歴を作成する.
     *
     * @return 小説の更新履歴
     */
    public NovelHistory createNovelHistory() {
        if (novelHistory == null) {
            novelHistory = new NovelHistory();
        }

        return novelHistory;
    }
}
