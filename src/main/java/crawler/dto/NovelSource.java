package crawler.dto;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import crawler.entity.Novel;
import crawler.entity.NovelHistory;
import crawler.exception.NovelNotFoundException;
import crawler.util.NovelElementsUtil;
import crawler.util.NovelManagerUtil;

/**
 * 小説のhtmlを保持するクラス.
 */
public class NovelSource extends BaseSource {

    /** 小説の情報 */
    private final Novel novel;

    /** 小説の更新履歴 */
    private NovelHistory novelHistory;

    /** 小説のURL */
    private final URL url;

    /** 小説のURLのホスト名 */
    private String hostname;

    /**
     * コンストラクタ.
     *
     * @param add
     *            true:新規、false:更新
     * @param url
     *            小説のURL
     * @param novel
     *            小説の情報
     * @throws NovelNotFoundException
     *             小説が見つからない
     */
    private NovelSource(final boolean add, final String url, final Novel novel) throws NovelNotFoundException {
        super(url, add);
        this.url = NovelManagerUtil.getUrl(url);
        this.novel = novel;
        // URLからホスト名を取得
        hostname = this.url.getProtocol() + "://" + this.url.getHost();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NovelSource mapping() {
        if (add) {
            // 小説の情報を設定
            novel.setTitle(NovelElementsUtil.getTitle(html));
            novel.setWritername(NovelElementsUtil.getWritername(html));
            novel.setDescription(NovelElementsUtil.getDescription(html));
            novel.setBody(NovelElementsUtil.getBody(html));
            novel.setUrl(url.toString());
            novel.setDeleted(false);
        } else {
            // 更新の場合、Historyを作成
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
            novel.setUpdateDate(LocalDateTime.now());
        }

        return this;
    }

    /**
     * タイトルに差異があるか確認し、差異があれば小説の更新履歴を作成する.
     */
    void checkTitleDiff() {
        String title = NovelElementsUtil.getTitle(html);

        if (!novel.getTitle().equals(title)) {
            createNovelHistory().setTitle(novel.getTitle());
            novel.setTitle(title);
        }
    }

    /**
     * 作者名に差異があるか確認し、差異があれば小説の更新履歴を作成する.
     */
    void checkWriternameDiff() {
        String writername = NovelElementsUtil.getWritername(html);

        if (!novel.getWritername().equals(writername)) {
            createNovelHistory().setWritername(novel.getWritername());
            novel.setWritername(writername);
        }
    }

    /**
     * 解説に差異があるか確認し、差異があれば小説の更新履歴を作成する.
     */
    void checkDescriptionDiff() {
        String description = NovelElementsUtil.getDescription(html);

        if (!novel.getDescription().equals(description)) {
            createNovelHistory().setDescription(novel.getDescription());
            novel.setDescription(description);
        }
    }

    /**
     * 本文に差異があるか確認し、差異があれば小説の更新履歴を作成する.
     */
    void checkBodyDiff() {
        String body = NovelElementsUtil.getBody(html);

        if (!novel.getBody().equals(body)) {
            createNovelHistory().setBody(novel.getBody());
            novel.setBody(body);
        }
    }

    /**
     * 小説の本文から小説の目次リストを取得する.
     *
     * @return 小説の目次リスト
     */
    public Stream<NovelIndexElement> getNovelIndexList() {
        return Jsoup.parse(novel.getBody()).getElementsByTag("dl").stream()
                .filter(chapterElement -> NovelElementsUtil.existsChapterLink(chapterElement))
                .map(chapterElement -> new NovelIndexElement(chapterElement));
    }

    /**
     * 小説の本文の履歴から小説の目次セットを取得する.
     *
     * @return 小説の目次セット
     */
    public Set<NovelIndexElement> getNovelHistoryIndexSet() {
        if (novelHistory != null) {
            Document novelHistoryBodyHtml = Jsoup.parse(novelHistory.getBody());
            Elements chapterHistoryElementList = novelHistoryBodyHtml.getElementsByTag("dl");

            if (chapterHistoryElementList.isEmpty()) {
                // 古いスタイルの場合
                chapterHistoryElementList = novelHistoryBodyHtml.getElementsByTag("tr");
            }

            return chapterHistoryElementList.stream()
                    .filter(chapterElement -> NovelElementsUtil.existsChapterLink(chapterElement))
                    .map(chapterElement -> new NovelIndexElement(chapterElement)).collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }

    /**
     * 小説の情報から小説の付随情報のURLを取得する.
     *
     * @return 小説の付随情報のURL
     */
    public String getNovelInfoUrl() {
        return NovelElementsUtil.getNovelInfoUrl(html);
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
     * 小説のUrlのホスト名を設定する.
     *
     * @param hostname
     *            小説のUrlのホスト名
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * NovelSourceのインスタンスを生成する.
     *
     * @param url
     *            小説のURL
     * @return NovelSourceのインスタンス
     * @throws NovelNotFoundException
     *             指定されたURLが取得出来ない
     */
    public static NovelSource newInstance(final String url) throws NovelNotFoundException {
        return newInstance(url, null);
    }

    /**
     * NovelSourceのインスタンスを生成する.
     *
     * @param url
     *            小説のURL
     * @param novel
     *            小説の情報
     * @return NovelSourceのインスタンス
     * @throws NovelNotFoundException
     *             指定されたURLが取得出来ない
     */
    public static NovelSource newInstance(final String url, final Novel novel) throws NovelNotFoundException {
        if (novel == null) {
            return new NovelSource(true, url, new Novel()).mapping();
        } else {
            return new NovelSource(false, url, novel).mapping();
        }
    }

    public Novel getNovel() {
        return novel;
    }

    public NovelHistory getNovelHistory() {
        return novelHistory;
    }

    /**
     * 小説の更新履歴を作成する.
     *
     * @return 小説の更新履歴
     */
    private NovelHistory createNovelHistory() {
        if (novelHistory == null) {
            novelHistory = new NovelHistory();
        }

        return novelHistory;
    }
}
