package crawler.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import crawler.dao.NovelDao;
import crawler.dao.UserDao;
import crawler.entity.Novel;
import crawler.entity.User;
import crawler.service.NovelOutputManager;
import crawler.service.mail.NovelReportMail;

/**
 * 小説の情報の出力を管理する.
 */
@Service("novelOutputManager")
public class NovelOutputManagerImpl extends BaseManagerImpl implements NovelOutputManager {

    /** ユーザーのDAO. */
    @Autowired
    private UserDao userDao;

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
    public List<User> getUnreadUserNovels() {
        try (Stream<User> novels = userDao.findByUnreadTrueOrderByTitleAndNovelChapterId()) {
            return novels.collect(Collectors.toList());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Novel> getModifiedDateOfNovels() {
        try (Stream<Novel> novels = novelDao.findByDeletedFalseOrderByTitle()) {
            return novels.collect(Collectors.toList());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void sendUnreadReport() {
        List<User> unreadUserNovels = getUnreadUserNovels();

        if (unreadUserNovels.isEmpty()) {
            log.info("Not find unread novels.");
        } else {
            unreadUserNovels.forEach(unreadUserNovel -> {
                // メール送信
                reportMail.sendUnreadReport(unreadUserNovel.getEmail(), unreadUserNovel.getUserNovelInfos());

                // 小説のステータスを既読に更新
                LocalDateTime now = LocalDateTime.now();
                unreadUserNovel.getUserNovelInfos().stream()
                        .flatMap(unreadNovel -> unreadNovel.getNovel().getNovelChapters().stream())
                        .forEach(unreadNovelChapter -> {
                            unreadNovelChapter.getNovelChapterInfo().setUnread(false);
                            unreadNovelChapter.getNovelChapterInfo().setReadDate(now);
                            unreadNovelChapter.getNovelChapterInfo().setUpdateDate(now);
                        });
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public void sendModifiedDateReport() {
        List<Novel> novels = getModifiedDateOfNovels();

        if (novels.isEmpty()) {
            log.info("Not find modified novels.");
        } else {
            // メール送信
            reportMail.sendModifiedDateReport(novels);
        }
    }
}
