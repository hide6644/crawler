package crawler.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawler.Constants;
import crawler.dao.NovelDao;
import crawler.domain.Novel;
import crawler.domain.NovelChapter;
import crawler.domain.NovelHistory;
import crawler.domain.NovelInfo;
import crawler.service.MailEngine;
import crawler.service.NovelChapterManager;
import crawler.service.NovelInfoManager;
import crawler.service.NovelManager;
import net.htmlparser.jericho.Source;

/**
 * 小説の情報を管理する.
 */
@Service("novelManager")
public class NovelManagerImpl extends GenericManagerImpl<Novel, Long> implements NovelManager {

    /** 小説DAO. */
    private NovelDao novelDao;

    /** 小説の付随情報. */
    @Autowired
    private NovelInfoManager novelInfoManager;

    /** 小説の章. */
    @Autowired
    private NovelChapterManager novelChapterManager;

    /** メールを作成するクラス */
    @Autowired
    private MailEngine mailEngine;

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#add(java.lang.String)
     */
    @Override
    public void add(final String url) {
        URL novelUrl = NovelManagerUtil.getUrl(url);

        // URLからhtmlを取得
        Source html = NovelManagerUtil.getSource(novelUrl);

        if (html != null) {
            // 小説の情報作成
            Novel novel = createNovel(url, html);

            // 小説の付随情報作成
            NovelInfo novelInfo = new NovelInfo();
            novelInfo.setCheckedDate(new Date());
            novelInfoManager.saveNovelInfo(html, novelInfo);
            novelInfo.setNovel(novel);
            novel.setNovelInfo(novelInfo);

            // 小説の章作成
            novelChapterManager.saveNovelChapter(novelUrl, html, novel);

            log.info("[add] title:" + novel.getTitle());
            save(novel);
        }
    }

    /**
     * 小説の情報を作成する.
     * 
     * @param url
     *            小説のURL
     * @param html
     *            小説のhtml要素
     * @return 小説の情報
     */
    private Novel createNovel(final String url, final Source html) {
        Novel novel = new Novel();
        novel.setTitle(NovelElementsUtil.getTitle(html));
        novel.setWritername(NovelElementsUtil.getWritername(html));
        novel.setDescription(NovelElementsUtil.getDescription(html));
        novel.setBody(NovelElementsUtil.getBody(html));
        novel.setUrl(url);
        novel.setDeleted(false);

        return novel;
    }

    /*
     * (非 Javadoc)
     * 
     * @see crawler.service.NovelManager#getCheckTargetId()
     */
    @Override
    public List<Long> getCheckTargetId() {
        List<Long> checkTargetNovels = new ArrayList<Long>();

        for (Novel savedNovel : novelDao.getNovelsByCheckedDate(new DateTime().withTimeAtStartOfDay().toDate())) {
            // 更新頻度から確認対象を絞り込む
            if (NovelManagerUtil.isConfirmedNovel(savedNovel)) {
                checkTargetNovels.add(savedNovel.getId());
            }
        }

        return checkTargetNovels;
    }

    /*
     * (非 Javadoc)
     * 
     * @see crawler.service.NovelManager#checkForUpdatesAndSaveHistory(java.lang.Long)
     */
    @Override
    public void checkForUpdatesAndSaveHistory(final Long savedNovelId) {
        Novel savedNovel = novelDao.get(savedNovelId);
        URL savedNovelUrl = NovelManagerUtil.getUrl(savedNovel.getUrl());

        // URLからhtmlを取得
        Source html = NovelManagerUtil.getSource(savedNovelUrl);

        if (html == null) {
            savedNovel.setDeleted(true);
            savedNovel.setUpdateDate(new Date());
            return;
        }

        Novel currentNovel = createNovel(savedNovel.getUrl(), html);

        log.info("[check] title:" + currentNovel.getTitle());
        NovelHistory novelHistory = createNovelHistory(savedNovelUrl, savedNovel, currentNovel);

        if (novelHistory != null) {
            // 差異がある場合
            novelInfoManager.saveNovelInfo(html, savedNovel.getNovelInfo());

            novelHistory.setNovel(savedNovel);
            savedNovel.addNovelHistory(novelHistory);
        }

        savedNovel.getNovelInfo().setCheckedDate(new Date());
        savedNovel.getNovelInfo().setUpdateDate(new Date());
        savedNovel.setUpdateDate(new Date());

        save(savedNovel);
    }

    /**
     * 小説の更新履歴を作成する.
     * 
     * @param savedNovelUrl
     *            小説のURL
     * @param savedNovel
     *            保存済みの小説の情報
     * @param currentNovel
     *            現在の小説の情報
     * @return 小説の更新履歴
     */
    private NovelHistory createNovelHistory(final URL savedNovelUrl, final Novel savedNovel, final Novel currentNovel) {
        NovelHistory novelHistory = null;

        if (!savedNovel.getTitle().equals(currentNovel.getTitle())) {
            if (novelHistory == null) {
                novelHistory = new NovelHistory();
            }
            // タイトルに差異がある場合
            novelHistory.setTitle(savedNovel.getTitle());
            savedNovel.setTitle(currentNovel.getTitle());
        }
        if (!savedNovel.getWritername().equals(currentNovel.getWritername())) {
            if (novelHistory == null) {
                novelHistory = new NovelHistory();
            }
            // 作者名に差異がある場合
            novelHistory.setWritername(savedNovel.getWritername());
            savedNovel.setWritername(currentNovel.getWritername());
        }
        if (!savedNovel.getDescription().equals(currentNovel.getDescription())) {
            if (novelHistory == null) {
                novelHistory = new NovelHistory();
            }
            // 解説に差異がある場合
            novelHistory.setDescription(savedNovel.getDescription());
            savedNovel.setDescription(currentNovel.getDescription());
        }
        if (!savedNovel.getBody().equals(currentNovel.getBody())) {
            if (novelHistory == null) {
                novelHistory = new NovelHistory();
            }
            // 本文に差異がある場合
            novelHistory.setBody(savedNovel.getBody());
            savedNovel.setBody(currentNovel.getBody());

            // 小説の章を取得
            novelChapterManager.saveNovelChapter(savedNovelUrl, new Source(currentNovel.getBody()), savedNovel, novelHistory);
        }

        return novelHistory;
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#getUnreadNovels()
     */
    @Override
    public List<Novel> getUnreadNovels() {
        return novelDao.getNovelsByUnread();
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#sendReport()
     */
    @Override
    public void sendReport() {
        List<Novel> unreadNovels = getUnreadNovels();

        if (unreadNovels.size() == 0) {
            log.info("not found.");
            return;
        }

        try {
            mailEngine.sendReportMail(createReport(unreadNovels));
        } catch (Exception e) {
            log.error("[error] send mail:", e);
        }
    }

    /**
     * 未読小説の一覧のファイルを作成する.
     * 
     * @param unreadNovels
     *            未読小説の一覧
     * @return ファイルパス
     */
    private String createReport(final List<Novel> unreadNovels) {
        String filePath = Constants.APP_FOLDER_NAME + Constants.FILE_SEP + "report" + Constants.FILE_SEP + new DateTime().minusDays(1).toString("yyyy-MM-dd") + ".html";
        PrintWriter pw = null;

        try {
            File file = new File(filePath);
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println("<html><head>");
            pw.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
            pw.println("<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no\" />");
            pw.println("</head><body><dl>");

            for (Novel unreadNovel : unreadNovels) {
                pw.println("<dt><a href='" + unreadNovel.getUrl() + "'>" + unreadNovel.getTitle() + "</a></dt>");

                for (NovelChapter unreadNovelChapter : unreadNovel.getNovelChapters()) {
                    if (unreadNovelChapter.getUpdateDate() == null || unreadNovelChapter.getCreateDate().equals(unreadNovelChapter.getUpdateDate())) {
                        log.info("[add] title:" + unreadNovel.getTitle() + " chapter title:" + unreadNovelChapter.getTitle());
                        pw.println("<dd>" + new DateTime(unreadNovelChapter.getNovelChapterInfo().getModifiedDate()).toString(ISODateTimeFormat.date()) + " <a href='" + unreadNovelChapter.getUrl()
                                + "'>" + unreadNovelChapter.getTitle() + "</a></dd>");
                    } else {
                        log.info("[update] title:" + unreadNovel.getTitle() + " chapter title:" + unreadNovelChapter.getTitle());
                        pw.println("<dd>" + new DateTime(unreadNovelChapter.getNovelChapterInfo().getModifiedDate()).toString(ISODateTimeFormat.date()) + " <a href='" + unreadNovelChapter.getUrl()
                                + "'>" + unreadNovelChapter.getTitle() + "</a> (更新)</dd>");
                    }

                    unreadNovelChapter.getNovelChapterInfo().setUnread(false);
                    unreadNovelChapter.getNovelChapterInfo().setReadDate(unreadNovelChapter.getNovelChapterInfo().getModifiedDate());
                    unreadNovelChapter.getNovelChapterInfo().setUpdateDate(new Date());
                }

                save(unreadNovel);
            }

            pw.println("</dl></body></html>");
        } catch (IOException e) {
            log.error("[error] report:", e);
        } finally {
            IOUtils.closeQuietly(pw);
        }

        return filePath;
    }

    /**
     * 小説DAOのインターフェイスを設定する.
     *
     * @param novelDao
     *            小説DAOのインターフェイス
     */
    @Autowired
    public void setNovelDao(NovelDao novelDao) {
        this.dao = novelDao;
        this.novelDao = novelDao;
    }
}
