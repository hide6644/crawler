package crawler.batch.impl;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * バッチ処理の基底.
 */
public abstract class BaseBatchProcess {

    /** ログ出力クラス */
    protected final Log log = LogFactory.getLog(getClass());

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
