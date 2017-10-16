package crawler.batch.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawler.batch.BatchProcess;
import crawler.service.NovelManager;

/**
 * 小説の情報を取得するバッチ処理を実行する.
 */
@Service("novelProcess")
public class NovelProcess extends BaseBatchProcess implements BatchProcess {

    /** 小説の情報を管理する. */
    @Autowired
    private NovelManager novelManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals(messages.getMessage("novelManager.getCheckTargetId"))) {
                    // 更新チェック
                    novelManager.getCheckTargetId().forEach(savedNovelId -> novelManager.checkForUpdatesAndSaveHistory(savedNovelId));
                } else if (args[i].equals(messages.getMessage("novelManager.sendReport"))) {
                    // 更新チェック結果を送信
                    novelManager.sendReport();
                } else if (args[i].startsWith(messages.getMessage("novelManager.add"))) {
                    // 小説を追加
                    novelManager.add(args[i].substring(args[i].indexOf('=') + 1).trim());
                } else if (args[i].startsWith(messages.getMessage("novelManager.delete"))) {
                    // 小説を削除
                    novelManager.delete(args[i].substring(args[i].indexOf('=') + 1).trim());
                } else {
                    // 不正な引数
                    throw new IllegalArgumentException();
                }
            }
        }
    }
}
