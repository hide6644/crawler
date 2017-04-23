package crawler.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawler.dao.NovelChapterDao;
import crawler.domain.NovelChapter;
import crawler.domain.source.NovelBodyIndexElement;
import crawler.domain.source.NovelChapterSource;
import crawler.domain.source.NovelSource;
import crawler.exception.NovelNotFoundException;
import crawler.service.NovelChapterInfoManager;
import crawler.service.NovelChapterManager;
import crawler.util.NovelManagerUtil;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveNovelChapter(final NovelSource novelSource) {
        String hostname = novelSource.getHostUrl();
        // 小説の履歴から小説の章のElementセットを作成し、変数に代入
        Set<NovelBodyIndexElement> chapterHistoryElementSet = novelSource.getChapterHistoryElementSet();

        novelSource.getChapterElementList().stream()
                .filter(novelBodyIndexElement -> NovelManagerUtil.hasUpdatedChapter(novelBodyIndexElement, chapterHistoryElementSet))
                .forEach(novelBodyIndexElement -> {
                    // 小説の章の情報に差異がある場合、小説の章を取得
                    try {
                        NovelChapterSource novelChapterSource = new NovelChapterSource(hostname + novelBodyIndexElement.getChapterUrl());

                        // URLが一致する小説の章を取得
                        novelChapterSource.setNovelChapter(novelChapterDao.getByUrl(novelChapterSource.getUrl().toString()));
                        novelChapterSource.mapping();

                        // 小説の章の付随情報を保存
                        novelChapterInfoManager.saveNovelChapterInfo(novelBodyIndexElement.getElement(), novelChapterSource);

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
                        log.info("[not found] chapter url:" + novelBodyIndexElement.getChapterUrl());
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
