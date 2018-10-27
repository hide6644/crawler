package crawler.service.mail;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawler.Constants;
import crawler.entity.Novel;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import no.api.freemarker.java8.Java8ObjectWrapper;

/**
 * Novel Reportメール処理クラス.
 */
@Service("novelReportMail")
public class NovelReportMail {

    /** 日付のフォーマット */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /** ログ出力クラス */
    private Logger log = LogManager.getLogger(getClass());

    /** メールを処理するクラス */
    @Autowired(required = false)
    private MailEngine mailEngine;

    /**
     * 未読小説の一覧のファイルを送信する.
     *
     * @param unreadNovels
     *            未読小説の一覧
     */
    public void sendUnreadReport(final List<Novel> unreadNovels) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("unreadNovels", unreadNovels);

        String yesterday = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        Path path = getPath("unread_novels_" + yesterday + ".html");
        String bodyText = yesterday + " updated.";

        sendReport("unread_report.ftl", dataModel, bodyText, path);
    }

    /**
     * 小説の最終更新日時一覧のファイルを送信する.
     *
     * @param novels
     *            小説の一覧
     */
    public void sendModifiedDateReport(final List<Novel> novels) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("novels", novels);

        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        Path path = getPath("modified_date_of_novels_" + today + ".html");
        String bodyText = "modified date of novels.";

        sendReport("modified_date_report.ftl", dataModel, bodyText, path);
    }

    /**
     * Pathオブジェクトを取得する.
     *
     * @param filePath
     *            ファイルパス
     * @return Pathオブジェクト
     */
    private Path getPath(String filePath) {
        Path path = Paths.get(Constants.APP_FOLDER_NAME + Constants.FILE_SEP + "report" + Constants.FILE_SEP + filePath);

        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            // file exists and is not a directory
            // parent may not exist or other reason
        }

        return path;
    }

    /**
     * ファイルを作成し送信する.
     *
     * @param templateName
     *            テンプレート名
     * @param dataModel
     *            テンプレートの変数（名前と値のペア）
     * @param bodyText
     *            メール本文
     * @param path
     *            Pathオブジェクト
     */
    private void sendReport(String templateName, Map<String, Object> dataModel, String bodyText, Path path) {
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8);) {
            // テンプレートとマージ
            getConfiguration().getTemplate(templateName).process(dataModel, bw);

            log.info("[send] report:" + bodyText);
            mailEngine.sendMail(bodyText, path.toFile());
        } catch (IOException | TemplateException | MessagingException e) {
            log.error("[not send] report:", e);
        }
    }

    /**
     * Freemarkerの構成を取得する.
     *
     * @return Freemarkerの構成
     */
    private Configuration getConfiguration() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setClassForTemplateLoading(getClass(), "/META-INF/freemarker/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setObjectWrapper(new Java8ObjectWrapper(Configuration.VERSION_2_3_28));
        return cfg;
    }
}
