package crawler.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import crawler.dao.NovelDao;
import crawler.dto.NovelSource;
import crawler.entity.Novel;
import crawler.exception.NovelConnectException;
import crawler.exception.NovelNotFoundException;
import crawler.service.NovelChapterManager;
import crawler.service.NovelInfoManager;
import crawler.service.NovelManager;

/**
 * 小説の情報を管理する.
 */
@Service("novelManager")
public class NovelManagerImpl extends BaseManagerImpl implements NovelManager {

    /** 小説のDAO. */
    @Autowired
    private NovelDao novelDao;

    /** 小説の付随情報を管理する. */
    @Autowired
    private NovelInfoManager novelInfoManager;

    /** 小説の章を管理する. */
    @Autowired
    private NovelChapterManager novelChapterManager;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void save(final String url) {
        Novel novel = novelDao.findByUrl(url);

        if (novel != null) {
            // 指定した小説が登録済みの場合
            log.info("[duplicate] title:{}", () -> novel.getTitle());
            checkForUpdatesAndSaveHistory(novel);
        } else {
            try {
                // 小説の情報を取得
                NovelSource novelSource = NovelSource.newInstance(url);
                log.info("[add] title:" + novelSource.getNovel().getTitle());

                // 小説の付随情報を保存
                novelInfoManager.saveNovelInfo(novelSource);

                // 小説の章を保存
                novelChapterManager.saveAllNovelChapter(novelSource);

                // 小説を永続化
                novelDao.save(novelSource.getNovel());
            } catch (NovelConnectException e) {
                log.warn("[skip] url:{}", url);
            } catch (NovelNotFoundException e) {
                log.warn("[deleted] url:{}", url);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void favorite(String url, boolean add) {
        Novel novel = novelDao.findByUrl(url);

        if (novel != null) {
            novel.getNovelInfo().setFavorite(add);
        } else {
            log.info("[not found] url:{}", url);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(final String url) {
        novelDao.delete(novelDao.findByUrl(url));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Long> getCheckTargetId() {
        // 更新頻度から確認対象を絞り込む
        return novelDao.findByDeletedFalseAndCheckedDateLessThanEqualAndCheckEnableTrue(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS))
                .filter(novel -> novel.getNovelInfo().needsCheckForUpdate())
                .map(novel -> novel.getId())
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void checkForUpdatesAndSaveHistory(final Long checkTargetId) {
        novelDao.findById(checkTargetId).ifPresent(novel -> {
            log.info("[check] title:{}", () -> novel.getTitle());
            checkForUpdatesAndSaveHistory(novel);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void checkForUpdatesAndSaveHistory(final Novel novel) {
        try {
            NovelSource currentNovelSource = NovelSource.newInstance(novel.getUrl(), novel);

            if (currentNovelSource.getNovelHistory() != null) {
                // 小説の情報に差異があった場合、小説の付随情報を保存
                novelInfoManager.saveNovelInfo(currentNovelSource);

                if (currentNovelSource.getNovelHistory().getBody() != null) {
                    // 小説の本文に差異があった場合、小説の章を保存
                    novelChapterManager.saveAllNovelChapter(currentNovelSource);
                }
            }
        } catch (NovelConnectException e) {
            log.warn("[skip] title:{}", () -> novel.getTitle());
        } catch (NovelNotFoundException e) {
            // 小説が取得出来ない場合、削除フラグを設定
            log.warn("[deleted] title:{}", () -> novel.getTitle());
            novel.setDeleted(true);
            novel.setUpdateDate(LocalDateTime.now());
        }
    }
}
