package crawler.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawler.dao.NovelChapterDao;
import crawler.domain.source.NovelChapterSource;
import crawler.domain.source.NovelIndexElement;
import crawler.domain.source.NovelSource;
import crawler.exception.NovelNotFoundException;
import crawler.service.NovelChapterInfoManager;
import crawler.service.NovelChapterManager;

/**
 * 小説の章を管理する.
 */
@Service("novelChapterManager")
public class NovelChapterManagerImpl extends BaseManagerImpl implements NovelChapterManager {

    /** 小説の章のDAO. */
    @Autowired
    private NovelChapterDao novelChapterDao;

    /** 小説の章の付随情報を管理する. */
    @Autowired
    private NovelChapterInfoManager novelChapterInfoManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAllNovelChapter(final NovelSource novelSource) {
        // 小説の本文の履歴から小説の目次のセットを取得
        Set<NovelIndexElement> novelHistoryIndexSet = novelSource.getNovelHistoryIndexSet();

        novelSource.getNovelIndexList().stream()
                // 小説の履歴が無い場合(新規の場合)、true:更新有りとする
                // 小説の目次のhtml elementが一致しない場合、true:更新有りとする
                .filter(novelIndexElement -> !novelHistoryIndexSet.contains(novelIndexElement))
                .forEach(novelIndexElement -> saveNovelChapter(novelSource, novelIndexElement));
    }

    /**
     * 小説の章を設定する.
     *
     * @param novelSource
     *            小説の情報
     * @param novelIndexElement
     *            小説の目次のhtml element
     */
    private void saveNovelChapter(final NovelSource novelSource, final NovelIndexElement novelIndexElement) {
        try {
            // 小説の章を取得
            String url = novelSource.getHostname() + novelIndexElement.getChapterUrl();
            NovelChapterSource novelChapterSource = NovelChapterSource.newInstance(url, novelChapterDao.findByUrl(url));

            // 小説の章の付随情報を設定
            novelChapterInfoManager.saveNovelChapterInfo(novelIndexElement, novelChapterSource);

            if (novelChapterSource.isAdd()) {
                // URLが一致する小説の章がない場合、登録処理
                novelChapterSource.getNovelChapter().setNovel(novelSource.getNovel());
                novelSource.getNovel().addNovelChapter(novelChapterSource.getNovelChapter());

                log.info("[add] chapter title:" + novelChapterSource.getNovelChapter().getTitle());
            } else {
                // 更新処理
                log.info("[update] chapter title:" + novelChapterSource.getNovelChapter().getTitle());
            }
        } catch (NovelNotFoundException e) {
            // 小説の章が取得出来ない場合、何もしない
            log.info("[not found] chapter url:" + novelIndexElement.getChapterUrl());
        }
    }
}
