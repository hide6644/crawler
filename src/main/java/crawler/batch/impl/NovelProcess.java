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
        if (args == null) {
            // 何もしない
            return;
        }

        for (int i = 0; i < args.length; i++) {
            executeNovelManager(args[i]);
        }
    }

    /**
     * バッチ処理分岐(NovelManager).
     *
     * @param arg
     *            引数
     */
    private void executeNovelManager(String arg) {
        if (arg.equals(messages.getMessage("novelManager.getCheckTargetId"))) {
            // 更新チェック
            novelManager.getCheckTargetId().forEach(savedNovelId -> novelManager.checkForUpdatesAndSaveHistory(savedNovelId));
        } else if (arg.equals(messages.getMessage("novelManager.sendReport"))) {
            // 更新チェック結果を送信
            novelManager.sendReport();
        } else if (arg.startsWith(messages.getMessage("novelManager.add"))) {
            // 小説を追加
            novelManager.add(arg.substring(arg.indexOf('=') + 1).trim());
        } else if (arg.startsWith(messages.getMessage("novelManager.delete"))) {
            // 小説を削除
            novelManager.delete(arg.substring(arg.indexOf('=') + 1).trim());
        } else {
            // 不正な引数
            throw new IllegalArgumentException();
        }
    }
}
