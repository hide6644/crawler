package crawler.batch.impl;

import java.net.MalformedURLException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawler.batch.BatchProcess;
import crawler.service.NovelManager;

/**
 * 小説のバッチ処理クラス.
 */
@Service("novelProcess")
public class NovelProcess extends BaseBatchProcess implements BatchProcess {

    /** 小説を管理するクラス */
    @Autowired
    private NovelManager novelManager;

    /*
     * (非 Javadoc)
     *
     * @see crawler.batch.BatchProcess#execute(java.lang.String[])
     */
    @Override
    public void execute(String[] args) throws MalformedURLException, MessagingException {
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("sendReport")) {
                    novelManager.sendReport();
                } else {
                    novelManager.save(args[i]);
                }
            }
        } else {
            novelManager.checkUpdate();
        }
    }
}
