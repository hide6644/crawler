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
        // 小説の情報を取得
        NovelSource novelSource = new NovelSource(url);
        novelSource.mapping();

        // 小説の付随情報を取得
        novelInfoManager.saveNovelInfo(novelSource);
        log.info("[add] title:" + novelSource.getNovel().getTitle());

        // 小説の章を取得
        novelChapterManager.saveNovelChapter(novelSource);

        save(novelSource.getNovel());
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
        return novelDao.getNovelsByCheckedDate(new DateTime().withTimeAtStartOfDay().toDate()).stream()
                .filter(savedNovel -> NovelManagerUtil.isConfirmedNovel(savedNovel))
                .map(savedNovel -> savedNovel.getId())
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
        Novel savedNovel = novelDao.get(checkTargetId);
        log.info("[check] title:" + savedNovel.getTitle());

        try {
            NovelSource currentNovelSource = new NovelSource(savedNovel.getUrl());

            currentNovelSource.setNovel(savedNovel);
            currentNovelSource.mapping();

            if (currentNovelSource.getNovelHistory() != null) {
                // 小説の情報に差異があった場合
                // 小説の付随情報を取得
                novelInfoManager.saveNovelInfo(currentNovelSource);

                if (currentNovelSource.getNovelHistory().getBody() != null) {
                    // 小説の本文に差異があった場合
                    // 小説の章を取得
                    novelChapterManager.saveNovelChapter(currentNovelSource);
                }

                save(currentNovelSource.getNovel());
            }
        } catch (NullPointerException e) {
            // ページが取得出来ない場合
            // 削除フラグを設定
            log.info("[deleted] title:" + savedNovel.getTitle());
            savedNovel.setDeleted(true);
            savedNovel.setUpdateDate(new Date());
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
        return novelDao.getNovelsByUnread();
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

        if (unreadNovels.size() == 0) {
            log.info("not found.");
            return;
        }

        try {
            // メール送信
            reportMail.sendUnreadNovelsReport(unreadNovels);

            unreadNovels.forEach(unreadNovel -> {
                unreadNovel.getNovelChapters().forEach(unreadNovelChapter -> {
                    unreadNovelChapter.getNovelChapterInfo().setUnread(false);
                    unreadNovelChapter.getNovelChapterInfo().setReadDate(unreadNovelChapter.getNovelChapterInfo().getModifiedDate());
                    unreadNovelChapter.getNovelChapterInfo().setUpdateDate(new Date());
                });

                save(unreadNovel);
            });
        } catch (Exception e) {
            log.error("[error] send mail:", e);
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
