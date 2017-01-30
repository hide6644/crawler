package crawler.service.impl;

import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawler.dao.NovelChapterDao;
import crawler.domain.NovelChapter;
import crawler.domain.source.NovelChapterSource;
import crawler.domain.source.NovelSource;
import crawler.service.NovelChapterInfoManager;
import crawler.service.NovelChapterManager;
import crawler.util.NovelElementsUtil;
import crawler.util.NovelElementsUtil.ContensType;
import crawler.util.NovelManagerUtil;
import net.htmlparser.jericho.Element;

/**
 * 小説の章を管理する.
 */
@Service("novelChapterManager")
public class NovelChapterManagerImpl extends GenericManagerImpl<NovelChapter, Long> implements NovelChapterManager {

    /** 小説の章のDAO. */
    private NovelChapterDao novelChapterDao;

    /** 小説の章の付随情報. */
    @Autowired
    private NovelChapterInfoManager novelChapterInfoManager;

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelChapterManager#saveNovelChapter(crawler.domain.source.NovelSource)
     */
    @Override
    public void saveNovelChapter(final NovelSource novelSource) {
        URL url = NovelManagerUtil.getUrl(novelSource.getUrl());
        // 小説の履歴から小説の章のElementリストを作成し、変数に代入
        List<Element> chapterHistoryElementList = novelSource.getChapterHistoryElementList();
        ContensType chapterHistoryElementContensType = novelSource.getChapterHistoryElementContensType();

        for (Element chapterElement : novelSource.getChapterElementList()) {
            // 小説の本文に含まれる章の数だけ繰り返す
            if (NovelElementsUtil.existsChapterLink(chapterElement) && NovelManagerUtil.hasUpdatedChapter(chapterElement, chapterHistoryElementList, chapterHistoryElementContensType)) {
                // 小説の章の情報に差異がある場合、小説の章を取得
                String chapterUrl = "http://" + url.getHost() + NovelElementsUtil.getChapterUrlByNovelBody(chapterElement);
                NovelChapterSource novelChapterSource = null;

                try {
                    novelChapterSource = new NovelChapterSource(chapterUrl);
                } catch (NullPointerException e) {
                    // ページが取得出来ない場合、何もしない
                    continue;
                }

                // URLが一致する小説の章を取得
                NovelChapter savedNovelChapter = novelChapterDao.getNovelChaptersByUrl(chapterUrl);
                novelChapterSource.setNovelChapter(savedNovelChapter);
                novelChapterSource.mapping();

                // 小説の章の付随情報を取得
                novelChapterInfoManager.saveNovelChapterInfo(chapterElement, novelChapterSource);

                if (savedNovelChapter == null) {
                    // URLが一致する小説の章がない場合、登録処理
                    novelChapterSource.getNovelChapter().setNovel(novelSource.getNovel());
                    novelSource.getNovel().addNovelChapter(novelChapterSource.getNovelChapter());

                    log.info("[add] chapter title:" + novelChapterSource.getNovelChapter().getTitle());
                } else {
                    // 更新処理
                    log.info("[update] chapter title:" + novelChapterSource.getNovelChapter().getTitle());
                }
            }
        }
    }

    /**
     * 小説の章のDAOのインターフェイスを設定する.
     *
     * @param novelChapterDao
     *            小説の章のDAOのインターフェイス
     */
    @Autowired
    public void setNovelChapterDao(NovelChapterDao novelChapterDao) {
        this.dao = novelChapterDao;
        this.novelChapterDao = novelChapterDao;
    }
}
