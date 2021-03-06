package crawler.batch.impl;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * バッチ処理の基底.
 */
public abstract class BaseBatchProcess {

    /** メッセージ取得クラス */
    protected MessageSourceAccessor messages;

    /**
     * メッセージソースを設定する.
     *
     * @param messageSource
     *            メッセージソース
     */
    @Autowired
    public void setMessages(MessageSource messageSource) {
        messages = new MessageSourceAccessor(messageSource, Locale.JAPAN);
    }
}
