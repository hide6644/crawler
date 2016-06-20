package crawler.batch.impl;

import java.net.MalformedURLException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawler.batch.BatchProcess;
import crawler.service.NovelManager;

/**
 * 小説の情報を取得するバッチ処理を実行する.
 */
@Service("novelProcess")
public class NovelProcess extends BaseBatchProcess implements BatchProcess {

    /** 小説の情報を管理する */
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
                if (args[i].equals("checkForUpdates")) {
                    for (Long savedNovelId : novelManager.getCheckTargetId()) {
                        novelManager.checkForUpdatesAndSaveHistory(savedNovelId);
                    }
                } else if (args[i].equals("sendReport")) {
                    novelManager.sendReport();
                } else {
                    novelManager.add(args[i]);
                }
            }
        }
    }
}
