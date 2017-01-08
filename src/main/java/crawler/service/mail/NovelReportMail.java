package crawler.service.mail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawler.Constants;
import crawler.domain.Novel;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Novel Reportメール処理クラス.
 */
@Service("reportMail")
public class NovelReportMail {

    /** ログ出力クラス */
    private final Logger log = LogManager.getLogger(getClass());

    /** メールを処理するクラス */
    @Autowired(required = false)
    private MailEngine mailEngine;

    /**
     * 未読小説の一覧のファイルを送信する.
     *
     * @param unreadNovels
     *            未読小説の一覧
     */
    public void sendUnreadNovelsReport(final List<Novel> unreadNovels) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setClassForTemplateLoading(getClass(), "/META-INF/freemarker/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);

        Map<String, Object> root = new HashMap<>();
        root.put("unreadNovels", unreadNovels);

        String filePath = Constants.APP_FOLDER_NAME + Constants.FILE_SEP + "report" + Constants.FILE_SEP + new DateTime().minusDays(1).toString("yyyy-MM-dd") + ".html";
        PrintWriter pw = null;

        try {
            File file = new File(filePath);
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

            // テンプレートとマージ
            cfg.getTemplate("report.ftl").process(root, pw);
            mailEngine.sendReportMail(filePath);
        } catch (IOException e) {
            log.error("[error] report:", e);
        } catch (TemplateException e) {
            log.error("[error] report:", e);
        } catch (MessagingException e) {
            log.error("[error] report:", e);
        } finally {
            IOUtils.closeQuietly(pw);
        }
    }
}
