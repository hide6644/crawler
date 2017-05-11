package crawler.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawler.dao.NovelChapterDao;
import crawler.domain.NovelChapter;
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
public class NovelChapterManagerImpl extends GenericManagerImpl<NovelChapter, Long> implements NovelChapterManager {

    /** 小説の章のDAO. */
    private NovelChapterDao novelChapterDao;

    /** 小説の章の付随情報を管理. */
    @Autowired
    private NovelChapterInfoManager novelChapterInfoManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveNovelChapter(final NovelSource novelSource) {
        String hostname = novelSource.getHostUrl();
        // 小説の本文の履歴から小説の目次のセットを取得
        Set<NovelIndexElement> novelHistoryIndexSet = novelSource.getNovelHistoryIndexSet();

        novelSource.getNovelIndexList().stream()
                // 小説の履歴が無い場合(新規の場合)、true:更新有りとする
                // 小説の目次のhtml element要素が一致しない場合、true:更新有りとする
                .filter(novelIndexElement -> novelHistoryIndexSet == null || !novelHistoryIndexSet.contains(novelIndexElement))
                .forEach(novelIndexElement -> {
                    try {
                        // 小説の章を取得
                        NovelChapterSource novelChapterSource = new NovelChapterSource(hostname + novelIndexElement.getChapterUrl());

                        // 履歴からURLが一致する小説の章を取得し設定
                        novelChapterSource.setNovelChapter(novelChapterDao.getByUrl(novelChapterSource.getUrl().toString()));
                        novelChapterSource.mapping();

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
                });
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
