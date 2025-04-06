package crawler.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawler.dao.NovelChapterDao;
import crawler.dao.NovelChapterHistoryDao;
import crawler.exception.NovelConnectException;
import crawler.exception.NovelNotFoundException;
import crawler.mapping.yomou.syosetu.com.NovelChapterSource;
import crawler.mapping.yomou.syosetu.com.NovelIndexElement;
import crawler.mapping.yomou.syosetu.com.NovelSource;
import crawler.service.NovelChapterInfoManager;
import crawler.service.NovelChapterManager;
import lombok.extern.log4j.Log4j2;

/**
 * 小説の章を管理する.
 */
@Service("novelChapterManager")
@Log4j2
public class NovelChapterManagerImpl extends BaseManagerImpl implements NovelChapterManager {

    /** 小説の章のDAO. */
    @Autowired
    private NovelChapterDao novelChapterDao;

    /** 小説の章の履歴のDAO. */
    @Autowired
    private NovelChapterHistoryDao novelChapterHistoryDao;

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

        novelSource.getNovelIndexList()
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
            var novelChapterSource = NovelChapterSource.newInstance(url, novelChapterDao.findByUrl(url));

            if (novelChapterSource.isAdd()) {
                // URLが一致する小説の章がない場合、登録処理
                novelSource.getNovel().addNovelChapter(novelChapterSource.getNovelChapter());

                log.info("[add] chapter title:{}", () -> novelChapterSource.getNovelChapter().getTitle());
            } else {
                // 小説の章の履歴を永続化
                novelChapterHistoryDao.save(novelChapterSource.getNovelChapterHistory());

                // 更新処理
                log.info("[update] chapter title:{}", () -> novelChapterSource.getNovelChapter().getTitle());
            }

            // 小説の章の付随情報を設定
            novelChapterInfoManager.saveNovelChapterInfo(novelIndexElement, novelChapterSource);
        } catch (NovelConnectException | NovelNotFoundException e) {
            // 小説の章が取得出来ない場合、何もしない
            log.info("[not found] chapter url:{}", () -> novelIndexElement.getChapterUrl());
        }
    }
}
