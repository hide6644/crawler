package crawler.service.impl;

import java.io.BufferedWriter;
import java.io.File;
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
import org.joda.time.format.DateTimeFormat;
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
import crawler.service.NovelManager;

/**
 * 小説を管理する.
 */
@Service("novelManager")
public class NovelManagerImpl extends GenericManagerImpl<Novel, Long> implements NovelManager {

    /** 小説DAOのインターフェイス. */
    private NovelDao novelDao;

    /** メールを作成するクラス */
    @Autowired
    private MailEngine mailEngine;

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#getReport()
     */
    @Override
    public void getReport() {
        List<Novel> unreadNovels = novelDao.getNovelsByUnread();

        if (unreadNovels.size() == 0) {
            log.info("not found.");
            return;
        }

        String filePath = Constants.APP_FOLDER_NAME + Constants.FILE_SEP + "report" + Constants.FILE_SEP
                + new DateTime().minusDays(1).toString("yyyy-MM-dd") + ".html";
        PrintWriter pw = null;

        try {
            File file = new File(filePath);
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">");
            pw.println("<dl>");

            for (Novel unreadNovel : unreadNovels) {
                pw.println("<dt><a href='" + unreadNovel.getUrl() + "'>" + unreadNovel.getTitle() + "</a></dt>");

                for (NovelChapter unreadNovelChapter : unreadNovel.getNovelChapters()) {
                    if (unreadNovelChapter.getUpdateDate() == null
                            || unreadNovelChapter.getCreateDate().equals(unreadNovelChapter.getUpdateDate())) {
                        log.info("[add] title:" + unreadNovel.getTitle() + " chapter title:"
                                + unreadNovelChapter.getTitle());
                        pw.println("<dd>"
                                + new DateTime(unreadNovelChapter.getModifiedDate()).toString(ISODateTimeFormat.date())
                                + " <a href='" + unreadNovelChapter.getUrl() + "'>" + unreadNovelChapter.getTitle()
                                + "</a></dd>");
                    } else {
                        log.info("[update] title:" + unreadNovel.getTitle() + " chapter title:"
                                + unreadNovelChapter.getTitle());
                        pw.println("<dd>"
                                + new DateTime(unreadNovelChapter.getModifiedDate()).toString(ISODateTimeFormat.date())
                                + " <a href='" + unreadNovelChapter.getUrl() + "'>" + unreadNovelChapter.getTitle()
                                + "</a> (更新)</dd>");
                    }

                    unreadNovelChapter.getNovelChapterInfo().setUnread(false);
                    unreadNovelChapter.getNovelChapterInfo().setReadDate(unreadNovelChapter.getModifiedDate());
                    unreadNovelChapter.getNovelChapterInfo().setUpdateDate(new Date());
                }

                novelDao.save(unreadNovel);
            }

            pw.println("</dl>");
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

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#create(java.lang.String)
     */
    @Override
    public void create(String url) {
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

        String title = html.getAllElementsByClass("novel_title").get(0).getTextExtractor().toString();
        String writername = html.getAllElementsByClass("novel_writername").get(0).getTextExtractor().toString()
                .replaceAll("作者：", "");
        String description = html.getElementById("novel_ex").getTextExtractor().toString();
        Element bodyElement = html.getAllElementsByClass("index_box").get(0);
        String body = bodyElement.toString();
        log.info("[add] title:" + title);

        Novel novel = new Novel();
        novel.setTitle(title);
        novel.setWritername(writername);
        novel.setDescription(description);
        novel.setBody(body);
        novel.setUrl(url);
        novel.setCheckedDate(new Date());
        novel.setDeleted(false);

        NovelInfo novelInfo = new NovelInfo();
        setNovelInfo(novel, novelInfo, html.getElementById("novel_header"));

        novelInfo.setNovel(novel);
        novel.setNovelInfo(novelInfo);

        // リンク先のコンテンツを取得
        for (Element element : bodyElement.getAllElements("a")) {
            String chapterUrl = "http://" + novelUrl.getHost() + element.getAttributeValue("href");
            Source chapterHtml = null;

            try {
                chapterHtml = new Source(new URL(chapterUrl));
            } catch (IOException e) {
                log.error("[error] url:" + chapterUrl, e);
            }

            chapterHtml.fullSequentialParse();

            List<Element> chapterTitleList = chapterHtml.getAllElementsByClass("novel_subtitle");
            if (chapterTitleList.size() == 0) {
                continue;
            }
            String chapterTitle = chapterTitleList.get(0).getTextExtractor().toString();
            String chapterBody = chapterHtml.getElementById("novel_honbun").toString();
            log.info("[add] chapterTitle:" + chapterTitle);

            NovelChapter novelChapter = new NovelChapter();
            novelChapter.setTitle(chapterTitle);
            novelChapter.setUrl(chapterUrl);
            novelChapter.setBody(chapterBody);
            novelChapter.setCheckedDate(new Date());
            setNovelChapterInfo(novelChapter,
                    element.getParentElement().getParentElement().getAllElementsByClass("long_update").get(0));

            NovelChapterInfo novelChapterInfo = new NovelChapterInfo();
            novelChapterInfo.setUnread(true);

            novelChapterInfo.setNovelChapter(novelChapter);
            novelChapter.setNovelChapterInfo(novelChapterInfo);

            novelChapter.setNovel(novel);
            novel.addNovelChapter(novelChapter);
        }

        novelDao.save(novel);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelManager#update()
     */
    @Override
    public void update() {
        List<Novel> checkNovels = novelDao.getNovelsByCheckedDate(new DateTime().withTimeAtStartOfDay().toDate());

        if (checkNovels.size() == 0) {
            log.info("not found.");
            return;
        }

        for (Novel checkNovel : checkNovels) {
            if (checkNovel.isFinished()) {
                if (new DateTime(checkNovel.getCheckedDate()).isAfter(new DateTime().minusDays(30))) {
                    log.info("[skip] finished title:" + checkNovel.getTitle());
                    continue;
                }
            }

            if (new DateTime(checkNovel.getModifiedDate()).isBefore(new DateTime().minusDays(30))) {
                if (new DateTime(checkNovel.getCheckedDate()).isAfter(new DateTime().minusDays(15))) {
                    log.info("[skip] title:" + checkNovel.getTitle());
                    continue;
                }
            }
            if (new DateTime(checkNovel.getModifiedDate()).isBefore(new DateTime().minusDays(14))) {
                if (new DateTime(checkNovel.getCheckedDate()).isAfter(new DateTime().minusDays(7))) {
                    log.info("[skip] title:" + checkNovel.getTitle());
                    continue;
                }
            }
            if (new DateTime(checkNovel.getModifiedDate()).isBefore(new DateTime().minusDays(3))) {
                if (new DateTime(checkNovel.getCheckedDate()).isAfter(new DateTime().minusDays(3))) {
                    log.info("[skip] title:" + checkNovel.getTitle());
                    continue;
                }
            }

            URL dispNovelUrl = null;
            Source html = null;

            try {
                dispNovelUrl = new URL(checkNovel.getUrl());
                html = new Source(dispNovelUrl);
            } catch (IOException e) {
                log.error("[error] url:" + checkNovel.getUrl(), e);
                checkNovel.setDeleted(true);
                checkNovel.setUpdateDate(new Date());
                continue;
            }

            html.fullSequentialParse();

            String title = html.getAllElementsByClass("novel_title").get(0).getTextExtractor().toString();
            String writername = html.getAllElementsByClass("novel_writername").get(0).getTextExtractor().toString()
                    .replaceAll("作者：", "");
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

                // 小説の章の差異を見つける
                for (Element chapter : bodySource.getAllElements("dl")) {
                    boolean modifiedFlag = true;
                    String subtitle = chapter.getAllElementsByClass("subtitle").get(0).getAllElements("a").get(0)
                            .toString();
                    String longUpdate = chapter.getAllElementsByClass("long_update").get(0).getTextExtractor()
                            .toString();

                    for (Element chapterHistory : htmlHistory.getAllElements("tr")) {
                        if (chapterHistory.getAllElementsByClass("period_subtitle").size() > 0) {
                            String subtitleHistory = chapterHistory.getAllElementsByClass("period_subtitle").get(0)
                                    .getAllElements("a").get(0).toString();
                            String longUpdateHistory = chapterHistory.getAllElementsByClass("long_update").get(0)
                                    .getTextExtractor().toString();

                            if (subtitle.equals(subtitleHistory) && longUpdate.equals(longUpdateHistory)) {
                                // 小説の章のHTML要素が一致する場合、変更なし
                                modifiedFlag = false;
                                break;
                            }
                        } else if (chapterHistory.getAllElementsByClass("long_subtitle").size() > 0) {
                            String subtitleHistory = chapterHistory.getAllElementsByClass("long_subtitle").get(0)
                                    .getAllElements("a").get(0).toString();
                            String longUpdateHistory = chapterHistory.getAllElementsByClass("long_update").get(0)
                                    .getTextExtractor().toString();

                            if (subtitle.equals(subtitleHistory) && longUpdate.equals(longUpdateHistory)) {
                                // 小説の章のHTML要素が一致する場合、変更なし
                                modifiedFlag = false;
                                break;
                            }
                        }
                    }
                    for (Element chapterHistory : htmlHistory.getAllElements("dl")) {
                        String subtitleHistory = chapterHistory.getAllElementsByClass("subtitle").get(0)
                                .getAllElements("a").get(0).toString();
                        String longUpdateHistory = chapterHistory.getAllElementsByClass("long_update").get(0)
                                .getTextExtractor().toString();

                        if (subtitle.equals(subtitleHistory) && longUpdate.equals(longUpdateHistory)) {
                            // 小説の章のHTML要素が一致する場合、変更なし
                            modifiedFlag = false;
                            break;
                        }
                    }

                    // 一致しなかった場合、変更あり
                    if (modifiedFlag && chapter.getAllElements("a").size() > 0) {
                        String chapterUrl = "http://" + dispNovelUrl.getHost()
                                + chapter.getAllElements("a").get(0).getAttributeValue("href");
                        Source chapterHtml = null;

                        try {
                            chapterHtml = new Source(new URL(chapterUrl));
                        } catch (IOException e) {
                            log.error("[error] url:" + chapterUrl, e);
                            continue;
                        }

                        chapterHtml.fullSequentialParse();

                        List<Element> chapterTitleList = chapterHtml.getAllElementsByClass("novel_subtitle");
                        if (chapterTitleList.size() == 0) {
                            continue;
                        }
                        String chapterTitle = chapterTitleList.get(0).getTextExtractor().toString();
                        String chapterBody = chapterHtml.getElementById("novel_honbun").toString();
                        boolean insertedFlag = true;

                        for (NovelChapter novelChapter : checkNovel.getNovelChapters()) {
                            if (chapterUrl.equals(novelChapter.getUrl())) {
                                // 一致するURLがある場合、更新処理
                                NovelChapterHistory novelChapterHistory = new NovelChapterHistory();

                                if (!novelChapter.getTitle().equals(chapterTitle)) {
                                    // タイトルに差異がある場合
                                    novelChapterHistory.setTitle(novelChapter.getTitle());
                                    novelChapter.setTitle(chapterTitle);
                                }

                                novelChapterHistory.setBody(novelChapter.getBody());
                                novelChapterHistory.setCheckedDate(novelChapter.getCheckedDate());
                                novelChapterHistory.setModifiedDate(novelChapter.getModifiedDate());
                                novelChapterHistory.setNovelChapter(novelChapter);

                                novelChapter.setBody(chapterBody);
                                novelChapter.setCheckedDate(new Date());
                                setNovelChapterInfo(novelChapter, chapter.getAllElementsByClass("long_update").get(0));
                                novelChapter.setUpdateDate(new Date());

                                // 未読フラグをtrueに更新する
                                novelChapter.getNovelChapterInfo().setUnread(true);
                                novelChapter.getNovelChapterInfo().setUpdateDate(new Date());

                                novelChapterHistory.setNovelChapter(novelChapter);
                                novelChapter.addNovelChapterHistory(novelChapterHistory);
                                insertedFlag = false;

                                log.info("[update] chapter title:" + chapterTitle);
                                break;
                            }
                        }

                        if (insertedFlag) {
                            // 登録処理
                            NovelChapter novelChapter = new NovelChapter();
                            novelChapter.setTitle(chapterTitle);
                            novelChapter.setUrl(chapterUrl);
                            novelChapter.setBody(chapterBody);
                            novelChapter.setCheckedDate(new Date());
                            setNovelChapterInfo(novelChapter, chapter.getAllElementsByClass("long_update").get(0));

                            NovelChapterInfo novelChapterInfo = new NovelChapterInfo();
                            // 未読フラグをtrueで登録する
                            novelChapterInfo.setUnread(true);

                            novelChapterInfo.setNovelChapter(novelChapter);
                            novelChapter.setNovelChapterInfo(novelChapterInfo);

                            novelChapter.setNovel(checkNovel);
                            checkNovel.addNovelChapter(novelChapter);

                            log.info("[add] chapter title:" + chapterTitle);
                        }
                    }
                }
            }

            if (novelHistory != null) {
                // 更新があった場合
                novelHistory.setCheckedDate(checkNovel.getCheckedDate());
                novelHistory.setModifiedDate(checkNovel.getModifiedDate());

                NovelInfo novelInfo = checkNovel.getNovelInfo();
                setNovelInfo(checkNovel, novelInfo, html.getElementById("novel_header"));
                novelInfo.setUpdateDate(new Date());

                novelHistory.setNovel(checkNovel);
                checkNovel.addNovelHistory(novelHistory);
            }

            checkNovel.setCheckedDate(new Date());
            checkNovel.setUpdateDate(new Date());

            novelDao.save(checkNovel);
        }
    }

    /**
     * 小説の付随情報を設定する.
     *
     * @param novel
     *            小説
     * @param novelInfo
     *            小説の付随情報
     * @param menuElement
     *            メニュー部分のhtml要素
     */
    private void setNovelInfo(Novel novel, NovelInfo novelInfo, Element menuElement) {
        for (Element linkElement : menuElement.getAllElements("a")) {
            if (linkElement.getTextExtractor().toString().equals("小説情報")) {
                try {
                    Source infoHtml = new Source(new URL(linkElement.getAttributeValue("href")));
                    infoHtml.fullSequentialParse();
                    String keyword = infoHtml.getElementById("noveltable1").getAllElements("td").get(2)
                            .getTextExtractor().toString();
                    String modifiedDate = infoHtml.getElementById("noveltable2").getAllElements("td").get(1)
                            .getTextExtractor().toString();
                    Element finishedElement = infoHtml.getElementById("noveltype");

                    novelInfo.setKeyword(keyword);
                    novel.setModifiedDate(DateTimeFormat.forPattern("yyyy年 MM月dd日 HH時mm分").parseDateTime(modifiedDate)
                            .toDate());

                    if (finishedElement != null && finishedElement.getTextExtractor().toString().equals("完結済")) {
                        novel.setFinished(true);
                    } else {
                        novel.setFinished(false);
                    }
                } catch (IOException e) {
                    log.error("[error] url:" + linkElement.getAttributeValue("href"), e);
                    continue;
                }
            }
        }
    }

    /**
     * 小説の章のリンクから更新日付を抽出、小説の章モデルに設定する.
     *
     * @param novelChapter
     *            小説の章
     * @param element
     *            小説の章の部分のhtml要素
     */
    private void setNovelChapterInfo(NovelChapter novelChapter, Element element) {
        if (element.getAllElements("span").size() > 0) {
            novelChapter.setModifiedDate(DateTimeFormat
                    .forPattern("yyyy年 MM月 dd日 改稿")
                    .parseDateTime(
                            element.getAllElements("span").get(0).getAttributeValue("title").toString())
                    .toDate());
        } else {
            novelChapter.setModifiedDate(DateTimeFormat.forPattern("yyyy年 MM月 dd日")
                    .parseDateTime(element.getTextExtractor().toString()).toDate());

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
