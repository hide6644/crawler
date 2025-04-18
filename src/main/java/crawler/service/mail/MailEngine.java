package crawler.service.mail;

import java.io.File;
import java.util.Optional;

import jakarta.mail.MessagingException;

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
     * @param bodyText
     *            本文
     * @param attachmentFile
     *            添付ファイル
     * @throws MessagingException
     *             {@link MessagingException}
     */
    public void sendMail(String bodyText, File attachmentFile) throws MessagingException {
        var message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
        var helper = new MimeMessageHelper(message, true);

        helper.setTo(Optional.ofNullable(mailMessage.getTo()).orElseThrow(() -> new NullPointerException()));
        helper.setFrom(Optional.ofNullable(mailMessage.getFrom()).orElseThrow(() -> new NullPointerException()));
        helper.setSubject(Optional.ofNullable(mailMessage.getSubject()).orElseThrow(() -> new NullPointerException()));
        helper.setText(bodyText);

        var file = new FileSystemResource(attachmentFile);
        helper.addAttachment(Optional.ofNullable(file.getFilename()).orElseThrow(() -> new NullPointerException()), file);

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
