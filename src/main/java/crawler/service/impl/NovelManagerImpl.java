package crawler.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import crawler.dao.NovelDao;
import crawler.domain.Novel;
import crawler.domain.source.NovelSource;
import crawler.service.NovelChapterManager;
import crawler.service.NovelInfoManager;
import crawler.service.NovelManager;
import crawler.service.mail.NovelReportMail;
import crawler.util.NovelManagerUtil;

/**
 * 小説の情報を管理する.
 */
@Service("novelManager")
public class NovelManagerImpl extends GenericManagerImpl<Novel, Long> implements NovelManager {

    /** 小説のDAO. */
    private NovelDao novelDao;

    /** 小説の付随情報. */
    @Autowired
    private NovelInfoManager novelInfoManager;

    /** 小説の章. */
    @Autowired
    private NovelChapterManager novelChapterManager;

    /** Reportメール処理クラス */
    @Autowired
    private NovelReportMail reportMail;

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#add(java.lang.String)
     */
    @Override
    @Transactional
    public void add(final String url) {
        Novel novel = novelDao.getByUrl(url);

        if (novel != null) {
            // 指定した小説が登録済みの場合
            log.info("[duplicate] title:" + novel.getTitle());
            checkForUpdatesAndSaveHistory(novel);
        } else {
            // 小説の情報を取得
            NovelSource novelSource = new NovelSource(url);
            novelSource.mapping();
            log.info("[add] title:" + novelSource.getNovel().getTitle());

            // 小説の付随情報を保存
            novelInfoManager.saveNovelInfo(novelSource);

            // 小説の章を保存
            novelChapterManager.saveNovelChapter(novelSource);

            // 小説を永続化
            save(novelSource.getNovel());
        }
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#getCheckTargetId()
     */
    @Override
    @Transactional(readOnly = true)
    public List<Long> getCheckTargetId() {
        // 更新頻度から確認対象を絞り込む
        return novelDao.getByCheckedDateLessThanEqual(new DateTime().withTimeAtStartOfDay().toDate()).stream()
                .filter(novel -> NovelManagerUtil.isConfirmedNovel(novel))
                .map(novel -> novel.getId())
                .collect(Collectors.toList());
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#checkForUpdatesAndSaveHistory(java.lang.Long)
     */
    @Override
    @Transactional
    public void checkForUpdatesAndSaveHistory(final Long checkTargetId) {
        Novel novel = novelDao.get(checkTargetId);
        log.info("[check] title:" + novel.getTitle());

        if (novel != null) {
            checkForUpdatesAndSaveHistory(novel);
        }
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#checkForUpdatesAndSaveHistory(crawler.domain.Novel)
     */
    @Override
    @Transactional
    public void checkForUpdatesAndSaveHistory(final Novel novel) {
        try {
            NovelSource currentNovelSource = new NovelSource(novel.getUrl());

            currentNovelSource.setNovel(novel);
            currentNovelSource.mapping();

            if (currentNovelSource.getNovelHistory() != null) {
                // 小説の情報に差異があった場合、小説の付随情報を保存
                novelInfoManager.saveNovelInfo(currentNovelSource);

                if (currentNovelSource.getNovelHistory().getBody() != null) {
                    // 小説の本文に差異があった場合、小説の章を保存
                    novelChapterManager.saveNovelChapter(currentNovelSource);
                }
            }
        } catch (NullPointerException e) {
            // ページが取得出来ない場合、削除フラグを設定
            log.info("[deleted] title:" + novel.getTitle());
            novel.setDeleted(true);
            novel.setUpdateDate(new Date());
        }
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#getUnreadNovels()
     */
    @Override
    @Transactional
    public List<Novel> getUnreadNovels() {
        return novelDao.getByUnreadTrueOrderByTitleAndId();
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#sendReport()
     */
    @Override
    @Transactional
    public void sendReport() {
        List<Novel> unreadNovels = getUnreadNovels();

        if (unreadNovels.size() > 0) {
            // メール送信
            reportMail.sendUnreadNovelsReport(unreadNovels);

            // 小説のステータスを既読に更新
            Date now = new Date();
            unreadNovels.stream().flatMap(unreadNovel -> unreadNovel.getNovelChapters().stream())
                    .forEach(unreadNovelChapter -> {
                        unreadNovelChapter.getNovelChapterInfo().setUnread(false);
                        unreadNovelChapter.getNovelChapterInfo().setReadDate(now);
                        unreadNovelChapter.getNovelChapterInfo().setUpdateDate(now);
                    });
        } else {
            log.info("Not find unread novels.");
        }
    }

    /**
     * 小説のDAOのインターフェイスを設定する.
     *
     * @param novelDao
     *            小説のDAOのインターフェイス
     */
    @Autowired
    public void setNovelDao(NovelDao novelDao) {
        this.dao = novelDao;
        this.novelDao = novelDao;
    }
}
