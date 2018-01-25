package crawler.service.mail;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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
     * メールを送信する.
     *
     * @param text
     *            メール本文
     * @param attachmentFile
     *            添付ファイル
     * @throws MessagingException
     *             {@link MessagingException}
     */
    public void sendMail(String text, File attachmentFile) throws MessagingException {
        MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(mailMessage.getTo());
        helper.setFrom(mailMessage.getFrom());

        helper.setText(text);
        helper.setSubject(mailMessage.getSubject());

        FileSystemResource file = new FileSystemResource(attachmentFile);
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
