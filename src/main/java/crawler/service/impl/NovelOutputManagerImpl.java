package crawler.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import crawler.dao.NovelDao;
import crawler.entity.Novel;
import crawler.service.NovelOutputManager;
import crawler.service.mail.NovelReportMail;

/**
 * 小説の情報の出力を管理する.
 */
@Service("novelOutputManager")
public class NovelOutputManagerImpl extends BaseManagerImpl implements NovelOutputManager {

    /** 小説のDAO. */
    @Autowired
    private NovelDao novelDao;

    /** Reportメール処理クラス */
    @Autowired
    private NovelReportMail reportMail;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Stream<Novel> getUnreadNovels() {
        return novelDao.findByUnreadTrueOrderByTitleAndNovelChapterId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Stream<Novel> getModifiedDateOfNovels() {
        return novelDao.findByDeletedFalseOrderByTitle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void sendUnreadReport() {
        List<Novel> unreadNovels = getUnreadNovels().collect(Collectors.toList());

        if (unreadNovels.isEmpty()) {
            log.info("Not find unread novels.");
        } else {
            // メール送信
            reportMail.sendUnreadReport(unreadNovels);

            // 小説のステータスを既読に更新
            LocalDateTime now = LocalDateTime.now();
            unreadNovels.stream().flatMap(unreadNovel -> unreadNovel.getNovelChapters().stream())
                    .forEach(unreadNovelChapter -> {
                        unreadNovelChapter.getNovelChapterInfo().setUnread(false);
                        unreadNovelChapter.getNovelChapterInfo().setReadDate(now);
                        unreadNovelChapter.getNovelChapterInfo().setUpdateDate(now);
                    });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void sendModifiedDateReport() {
        List<Novel> novels = getModifiedDateOfNovels().collect(Collectors.toList());

        if (novels.isEmpty()) {
            log.info("Not find novels.");
        } else {
            // メール送信
            reportMail.sendModifiedDateReport(novels);
        }
    }
}
