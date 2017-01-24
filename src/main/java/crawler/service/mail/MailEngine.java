package crawler.service.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * メールを作成するクラス.
 */
public class MailEngine {

    /** メール送信処理クラス */
    private MailSender mailSender;

    /** メールメッセージモデル */
    @Autowired
    private SimpleMailMessage mailMessage;

    /**
     * レポートメールを送信する.
     *
     * @param attachmentFilePath
     *            添付ファイルのパス
     * @throws MessagingException
     *             {@link MessagingException}
     */
    public void sendReportMail(String attachmentFilePath) throws MessagingException {
        MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(mailMessage.getTo());
        helper.setFrom(mailMessage.getFrom());

        helper.setText(new DateTime().minusDays(1).toString("yyyy-MM-dd") + " updated.");
        helper.setSubject(mailMessage.getSubject());

        FileSystemResource file = new FileSystemResource(attachmentFilePath);
        helper.addAttachment(file.getFilename(), file);

        ((JavaMailSenderImpl) mailSender).send(message);
    }

    /**
     * メール送信処理クラスを設定する.
     *
     * @param mailSender
     *            メール送信処理クラス
     */
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
}
