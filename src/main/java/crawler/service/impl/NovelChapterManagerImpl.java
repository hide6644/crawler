package crawler.service.impl;

import java.net.URL;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawler.domain.Novel;
import crawler.domain.NovelChapter;
import crawler.domain.NovelChapterHistory;
import crawler.domain.NovelChapterInfo;
import crawler.domain.NovelHistory;
import crawler.service.NovelChapterInfoManager;
import crawler.service.NovelChapterManager;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

/**
 * 小説の章を管理する.
 */
@Service("novelChapterManager")
public class NovelChapterManagerImpl extends GenericManagerImpl<NovelChapter, Long> implements NovelChapterManager {

    /** 小説の章の付随情報. */
    @Autowired
    private NovelChapterInfoManager novelChapterInfoManager;

    /* (非 Javadoc)
     * 
     * @see crawler.service.NovelChapterManager#saveNovelChapter(java.net.URL, net.htmlparser.jericho.Source, crawler.domain.Novel)
     */
    @Override
    public void saveNovelChapter(final URL url, final Source html, final Novel novel) {
        for (Element chapterElement : html.getAllElements("dl")) {
            String chapterUrl = "http://" + url.getHost() + NovelElementsUtil.getChapterUrlByNovelBody(chapterElement);

            // URLからhtmlを取得
            Source chapterHtml = NovelManagerUtil.getSource(NovelManagerUtil.getUrl(chapterUrl));

            if (NovelElementsUtil.existsChapter(chapterHtml)) {
                // コンテンツが存在する場合
                // 小説の章の情報を作成
                NovelChapter novelChapter = createNovelChapter(chapterUrl, chapterHtml);

                // 小説の章の付随情報を作成
                NovelChapterInfo novelChapterInfo = novelChapterInfoManager.saveNovelChapterInfo(chapterElement, novelChapter);

                novelChapterInfo.setNovelChapter(novelChapter);
                novelChapter.setNovelChapterInfo(novelChapterInfo);

                novelChapter.setNovel(novel);
                novel.addNovelChapter(novelChapter);
                log.info("[add] chapterTitle:" + novelChapter.getTitle());
            }
        }
    }

    /**
     * 小説の章の情報を作成する.
     * 
     * @param url
     *            小説の章のURL
     * @param html
     *            小説の章のhtml要素
     * @return 小説の章の情報
     */
    private NovelChapter createNovelChapter(final String url, final Source html) {
        NovelChapter novelChapter = new NovelChapter();
        novelChapter.setTitle(NovelElementsUtil.getChapterTitle(html));
        novelChapter.setUrl(url);
        novelChapter.setBody(NovelElementsUtil.getChapterBody(html));

        return novelChapter;
    }

    /* (非 Javadoc)
     * 
     * @see crawler.service.NovelChapterManager#saveNovelChapter(java.net.URL, net.htmlparser.jericho.Source, crawler.domain.Novel, crawler.domain.NovelHistory)
     */
    @Override
    public void saveNovelChapter(final URL url, final Source html, final Novel novel, final NovelHistory novelHistory) {
        for (Element chapterElement : html.getAllElements("dl")) {
            if (NovelElementsUtil.existsChapterLink(chapterElement) && NovelManagerUtil.hasUpdatedChapter(chapterElement, novelHistory)) {
                // 小説の章の情報に差異がある場合
                String chapterUrl = "http://" + url.getHost() + NovelElementsUtil.getChapterUrlByNovelBody(chapterElement);

                // URLからhtmlを取得
                Source chapterHtml = NovelManagerUtil.getSource(NovelManagerUtil.getUrl(chapterUrl));

                if (NovelElementsUtil.existsChapter(chapterHtml)) {
                    // コンテンツが存在する場合
                    // 小説の章の情報を作成
                    NovelChapter currentNovelChapter = createNovelChapter(chapterUrl, chapterHtml);

                    // URLが一致する小説の章を履歴から取得
                    NovelChapter savedNovelChapter = getSameNovelChapter(chapterUrl, novel);

                    // 小説の章の付随情報を作成
                    NovelChapterInfo novelChapterInfo = novelChapterInfoManager.saveNovelChapterInfo(chapterElement, savedNovelChapter);

                    if (savedNovelChapter != null) {
                        // 一致するURLがある場合、更新処理
                        savedNovelChapter.setUpdateDate(new Date());

                        NovelChapterHistory novelChapterHistory = createNovelChapterHistory(savedNovelChapter, currentNovelChapter);

                        novelChapterHistory.setNovelChapter(savedNovelChapter);
                        savedNovelChapter.addNovelChapterHistory(novelChapterHistory);

                        log.info("[update] chapter title:" + savedNovelChapter.getTitle());
                    } else {
                        // 登録処理
                        currentNovelChapter.setNovel(novel);
                        novel.addNovelChapter(currentNovelChapter);

                        novelChapterInfo.setNovelChapter(currentNovelChapter);
                        currentNovelChapter.setNovelChapterInfo(novelChapterInfo);

                        log.info("[add] chapter title:" + currentNovelChapter.getTitle());
                    }
                }
            }
        }
    }

    /**
     * URLに一致する小説の章の情報を履歴から取得する.
     * 
     * @param chapterUrl
     *            小説の章のURL
     * @param novel
     *            小説の情報
     * @return 小説の章の情報
     */
    private NovelChapter getSameNovelChapter(final String chapterUrl, final Novel novel) {
        for (NovelChapter novelChapter : novel.getNovelChapters()) {
            if (chapterUrl.equals(novelChapter.getUrl())) {
                return novelChapter;
            }
        }

        return null;
    }

    /**
     * 小説の章の更新履歴を作成する.
     * 
     * @param savedNovelChapter
     *            保存済みの小説の章の情報
     * @param currentNovelChapter
     *            現在の小説の章の情報
     * @return 小説の章の更新履歴
     */
    private NovelChapterHistory createNovelChapterHistory(final NovelChapter savedNovelChapter, final NovelChapter currentNovelChapter) {
        NovelChapterHistory novelChapterHistory = new NovelChapterHistory();

        if (!savedNovelChapter.getTitle().equals(currentNovelChapter.getTitle())) {
            // タイトルに差異がある場合
            novelChapterHistory.setTitle(savedNovelChapter.getTitle());
            savedNovelChapter.setTitle(currentNovelChapter.getTitle());
        }

        novelChapterHistory.setBody(savedNovelChapter.getBody());
        savedNovelChapter.setBody(currentNovelChapter.getBody());

        return novelChapterHistory;
    }
}
