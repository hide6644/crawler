package crawler.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import crawler.dao.NovelDao;
import crawler.domain.Novel;
import crawler.service.NovelOutputManager;
import crawler.service.mail.NovelReportMail;

/**
 * 小説の情報の出力を管理する.
 */
@Service("novelOutputManager")
public class NovelOutputManagerImpl extends GenericManagerImpl<Novel, Long> implements NovelOutputManager {

    /** 小説のDAO. */
    private NovelDao novelDao;

    /** Reportメール処理クラス */
    @Autowired
    private NovelReportMail reportMail;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Novel> getUnreadNovels() {
        return novelDao.getByUnreadTrueOrderByTitleAndId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void sendReport() {
        List<Novel> unreadNovels = getUnreadNovels();

        if (unreadNovels.isEmpty()) {
            log.info("Not find unread novels.");
        } else {
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
