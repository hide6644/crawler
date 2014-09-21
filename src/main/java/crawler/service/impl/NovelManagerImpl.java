package crawler.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawler.Constants;
import crawler.dao.NovelDao;
import crawler.domain.Novel;
import crawler.domain.NovelChapter;
import crawler.domain.NovelChapterHistory;
import crawler.domain.NovelChapterInfo;
import crawler.domain.NovelHistory;
import crawler.domain.NovelInfo;
import crawler.service.MailEngine;
import crawler.service.NovelChapterInfoManager;
import crawler.service.NovelInfoManager;
import crawler.service.NovelManager;

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

    /** 小説の章の付随情報. */
    @Autowired
    private NovelChapterInfoManager novelChapterInfoManager;

    /** メールを作成するクラス */
    @Autowired
    private MailEngine mailEngine;

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#getNovelsByUnread()
     */
    @Override
    public List<Novel> getNovelsByUnread() {
        return novelDao.getNovelsByUnread();
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#save(java.lang.String)
     */
    @Override
    public void save(String url) {
        URL novelUrl = null;
        Source html = null;

        try {
            novelUrl = new URL(url);
            html = new Source(novelUrl);
        } catch (IOException e) {
            log.error("[error] url:" + url, e);
            return;
        }

        html.fullSequentialParse();

        // タイトルや目次などのコンテンツを取得
        String title = html.getAllElementsByClass("novel_title").get(0).getTextExtractor().toString();
        String writername = html.getAllElementsByClass("novel_writername").get(0).getTextExtractor().toString().replaceAll("作者：", "");
        String description = html.getElementById("novel_ex").getTextExtractor().toString();
        Element bodyElement = html.getAllElementsByClass("index_box").get(0);
        log.info("[add] title:" + title);

        // 小説の情報作成
        Novel novel = new Novel();
        novel.setTitle(title);
        novel.setWritername(writername);
        novel.setDescription(description);
        novel.setBody(bodyElement.toString());
        novel.setUrl(url);
        novel.setDeleted(false);

        // 小説の付随情報作成
        NovelInfo novelInfo = new NovelInfo();
        novelInfo.setCheckedDate(new Date());
        novelInfoManager.setNovelInfo(novelInfo, html);

        novelInfo.setNovel(novel);
        novel.setNovelInfo(novelInfo);

        // リンク先のコンテンツを取得
        for (Element element : bodyElement.getAllElements("a")) {
            String chapterUrl = "http://" + novelUrl.getHost() + element.getAttributeValue("href");
            Source chapterHtml = null;

            try {
                synchronized (this) {
                    wait(1000);
                }
                chapterHtml = new Source(new URL(chapterUrl));
            } catch (IOException e) {
                log.error("[error] url:" + chapterUrl, e);
                continue;
            } catch (InterruptedException e) {
                log.error("[error] url:" + chapterUrl, e);
                continue;
            }

            chapterHtml.fullSequentialParse();

            List<Element> chapterTitleList = chapterHtml.getAllElementsByClass("novel_subtitle");
            if (chapterTitleList.size() > 0) {
                // コンテンツが存在する場合
                String chapterTitle = chapterTitleList.get(0).getTextExtractor().toString();
                String chapterBody = chapterHtml.getElementById("novel_honbun").toString();

                // 小説の章の情報を作成
                NovelChapter novelChapter = new NovelChapter();
                novelChapter.setTitle(chapterTitle);
                novelChapter.setUrl(chapterUrl);
                novelChapter.setBody(chapterBody);

                // 小説の章の付随情報を作成
                NovelChapterInfo novelChapterInfo = new NovelChapterInfo();
                novelChapterInfo.setCheckedDate(new Date());
                novelChapterInfo.setUnread(true);
                novelChapterInfoManager.setModifiedDate(novelChapterInfo, element.getParentElement().getParentElement());

                novelChapterInfo.setNovelChapter(novelChapter);
                novelChapter.setNovelChapterInfo(novelChapterInfo);

                novelChapter.setNovel(novel);
                novel.addNovelChapter(novelChapter);
                log.info("[add] chapterTitle:" + chapterTitle);
            }
        }

        save(novel);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#checkUpdate()
     */
    @Override
    public void checkUpdate() {
        List<Novel> checkNovels = novelDao.getNovelsByCheckedDate(new DateTime().withTimeAtStartOfDay().toDate());

        if (checkNovels.size() == 0) {
            log.info("not found.");
            return;
        }

        for (Novel checkNovel : checkNovels) {
            // 更新頻度から確認対象を絞り込む
            if (NovelManagerUtil.checkUpdateFrequency(checkNovel)) {
                URL dispNovelUrl = null;
                Source html = null;

                try {
                    dispNovelUrl = new URL(checkNovel.getUrl());
                    html = new Source(dispNovelUrl);
                } catch (FileNotFoundException e) {
                    log.error("[error] url:" + checkNovel.getUrl(), e);
                    checkNovel.setDeleted(true);
                    checkNovel.setUpdateDate(new Date());
                    continue;
                } catch (IOException e) {
                    log.error("[error] url:" + checkNovel.getUrl(), e);
                    continue;
                }

                html.fullSequentialParse();

                String title = html.getAllElementsByClass("novel_title").get(0).getTextExtractor().toString();
                String writername = html.getAllElementsByClass("novel_writername").get(0).getTextExtractor().toString().replaceAll("作者：", "");
                String description = html.getElementById("novel_ex").getTextExtractor().toString();
                String body = html.getAllElementsByClass("index_box").get(0).toString();
                log.info("[check] title:" + title);

                NovelHistory novelHistory = null;

                if (!checkNovel.getTitle().equals(title)) {
                    // タイトルに差異がある場合
                    novelHistory = new NovelHistory();
                    novelHistory.setTitle(checkNovel.getTitle());
                    checkNovel.setTitle(title);
                }

                if (!checkNovel.getWritername().equals(writername)) {
                    // 作者名に差異がある場合
                    if (novelHistory == null) {
                        novelHistory = new NovelHistory();
                    }
                    novelHistory.setWritername(checkNovel.getWritername());
                    checkNovel.setWritername(writername);
                }

                if (!checkNovel.getDescription().equals(description)) {
                    // 解説に差異がある場合
                    if (novelHistory == null) {
                        novelHistory = new NovelHistory();
                    }
                    novelHistory.setDescription(checkNovel.getDescription());
                    checkNovel.setDescription(description);
                }

                if (!checkNovel.getBody().equals(body)) {
                    // 本文に差異がある場合
                    if (novelHistory == null) {
                        novelHistory = new NovelHistory();
                    }
                    novelHistory.setBody(checkNovel.getBody());
                    checkNovel.setBody(body);

                    Source bodySource = new Source(body);
                    Source htmlHistory = new Source(novelHistory.getBody());

                    for (Element chapter : bodySource.getAllElements("dl")) {
                        String subtitle = chapter.getAllElementsByClass("subtitle").get(0).getAllElements("a").get(0).toString();
                        String chapterUpdateDate = chapter.getAllElementsByClass("long_update").get(0).getTextExtractor().toString();

                        if (NovelManagerUtil.checkChapterUpdate(subtitle, chapterUpdateDate, htmlHistory) && chapter.getAllElements("a").size() > 0) {
                            // 小説の章の情報に差異がある場合
                            String chapterUrl = "http://" + dispNovelUrl.getHost() + chapter.getAllElements("a").get(0).getAttributeValue("href");
                            Source chapterHtml = null;

                            try {
                                synchronized (this) {
                                    wait(1000);
                                }
                                chapterHtml = new Source(new URL(chapterUrl));
                            } catch (IOException e) {
                                log.error("[error] url:" + chapterUrl, e);
                                continue;
                            } catch (InterruptedException e) {
                                log.error("[error] url:" + chapterUrl, e);
                                continue;
                            }

                            chapterHtml.fullSequentialParse();

                            List<Element> chapterTitleList = chapterHtml.getAllElementsByClass("novel_subtitle");
                            if (chapterTitleList.size() > 0) {
                                // コンテンツが存在する場合
                                String chapterTitle = chapterTitleList.get(0).getTextExtractor().toString();
                                String chapterBody = chapterHtml.getElementById("novel_honbun").toString();
                                saveChapter(chapterUrl, chapterTitle, chapterBody, checkNovel, chapter);
                            }
                        }
                    }
                }

                if (novelHistory != null) {
                    // 差異があった場合
                    novelInfoManager.setNovelInfo(checkNovel.getNovelInfo(), html);

                    novelHistory.setNovel(checkNovel);
                    checkNovel.addNovelHistory(novelHistory);
                }

                checkNovel.getNovelInfo().setCheckedDate(new Date());
                checkNovel.getNovelInfo().setUpdateDate(new Date());
                checkNovel.setUpdateDate(new Date());

                save(checkNovel);
            }
        }
    }

    /**
     * 小説の章の情報を保存する.
     *
     * @param chapterUrl
     *            小説の章のURL
     * @param chapterTitle
     *            小説の章のタイトル
     * @param chapterBody
     *            小説の章の本文
     * @param novel
     *            小説の情報
     * @param chapterElement
     *            小説の章の部分のhtml要素
     */
    private void saveChapter(String chapterUrl, String chapterTitle, String chapterBody, Novel novel, Element chapterElement) {
        for (NovelChapter novelChapter : novel.getNovelChapters()) {
            if (chapterUrl.equals(novelChapter.getUrl())) {
                // 一致するURLがある場合、更新処理
                NovelChapterHistory novelChapterHistory = new NovelChapterHistory();

                if (!novelChapter.getTitle().equals(chapterTitle)) {
                    // タイトルに差異がある場合
                    novelChapterHistory.setTitle(novelChapter.getTitle());
                    novelChapter.setTitle(chapterTitle);
                }

                novelChapterHistory.setBody(novelChapter.getBody());
                novelChapterHistory.setNovelChapter(novelChapter);

                novelChapter.setBody(chapterBody);
                novelChapter.setUpdateDate(new Date());

                novelChapter.getNovelChapterInfo().setCheckedDate(new Date());
                // 未読フラグをtrueに更新する
                novelChapter.getNovelChapterInfo().setUnread(true);
                novelChapter.getNovelChapterInfo().setUpdateDate(new Date());
                novelChapterInfoManager.setModifiedDate(novelChapter.getNovelChapterInfo(), chapterElement);

                novelChapterHistory.setNovelChapter(novelChapter);
                novelChapter.addNovelChapterHistory(novelChapterHistory);
                log.info("[update] chapter title:" + chapterTitle);
                return;
            }
        }

        // 登録処理
        NovelChapter novelChapter = new NovelChapter();
        novelChapter.setTitle(chapterTitle);
        novelChapter.setUrl(chapterUrl);
        novelChapter.setBody(chapterBody);

        NovelChapterInfo novelChapterInfo = new NovelChapterInfo();
        novelChapterInfo.setCheckedDate(new Date());
        // 未読フラグをtrueで登録する
        novelChapterInfo.setUnread(true);
        novelChapterInfoManager.setModifiedDate(novelChapterInfo, chapterElement);

        novelChapterInfo.setNovelChapter(novelChapter);
        novelChapter.setNovelChapterInfo(novelChapterInfo);

        novelChapter.setNovel(novel);
        novel.addNovelChapter(novelChapter);
        log.info("[add] chapter title:" + chapterTitle);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#sendReport()
     */
    @Override
    public void sendReport() {
        List<Novel> unreadNovels = getNovelsByUnread();

        if (unreadNovels.size() == 0) {
            log.info("not found.");
            return;
        }

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
            pw.println("</head><dl>");

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

            pw.println("</dl></html>");
        } catch (IOException e) {
            log.error("[error] report:", e);
        } finally {
            IOUtils.closeQuietly(pw);
        }

        try {
            mailEngine.sendReportMail(filePath);
        } catch (Exception e) {
            log.error("[error] send mail:", e);
        }
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
