package crawler.batch.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawler.batch.BatchProcess;
import crawler.service.NovelManager;
import crawler.service.NovelOutputManager;

/**
 * 小説の情報を取得するバッチ処理を実行する.
 */
@Service("novelProcess")
public class NovelProcess extends BaseBatchProcess implements BatchProcess {

    /** 小説の情報を管理する. */
    @Autowired
    private NovelManager novelManager;

    /** 小説の情報の出力を管理する. */
    @Autowired
    private NovelOutputManager novelOutputManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(String[] args) {
        if (args == null) {
            // 何もしない
            return;
        }

        Set<Boolean> executeFlag = new HashSet<>();

        for (int i = 0; i < args.length; i++) {
            executeFlag.add(executeNovelManager(args[i]));
            executeFlag.add(executeNovelOutputManager(args[i]));
        }

        if (!executeFlag.contains(true)) {
            // 不正な引数
            throw new IllegalArgumentException();
        }
    }

    /**
     * バッチ処理分岐(NovelManager).
     *
     * @param arg
     *            引数
     * @return true:処理実行済み、false:処理未実行
     */
    private boolean executeNovelManager(String arg) {
        boolean executeFlag = true;

        if (arg.equals(messages.getMessage("novelManager.getCheckTargetId"))) {
            // 更新チェック
            novelManager.getCheckTargetId().forEach(savedNovelId -> novelManager.checkForUpdatesAndSaveHistory(savedNovelId));
        } else if (arg.startsWith(messages.getMessage("novelManager.save"))) {
            // 小説を追加、既に存在する場合は更新
            novelManager.save(substringUrl(arg));
        } else if (arg.startsWith(messages.getMessage("novelManager.favorite"))) {
            // 小説をお気に入りに指定
            novelManager.favorite(substringUrl(arg), true);
        } else if (arg.startsWith(messages.getMessage("novelManager.unfavorite"))) {
            // 小説のお気に入りを解除
            novelManager.favorite(substringUrl(arg), false);
        } else if (arg.startsWith(messages.getMessage("novelManager.delete"))) {
            // 小説を削除
            novelManager.delete(substringUrl(arg));
        } else {
            executeFlag = false;
        }

        return executeFlag;
    }

    /**
     * バッチ処理分岐(NovelOutputManager).
     *
     * @param arg
     *            引数
     * @return true:処理実行済み、false:処理未実行
     */
    private boolean executeNovelOutputManager(String arg) {
        boolean executeFlag = true;

        if (arg.equals(messages.getMessage("novelOutputManager.sendUnreadReport"))) {
            // 未読小説の一覧をメールで送信
            novelOutputManager.sendUnreadReport();
        } else if (arg.equals(messages.getMessage("novelOutputManager.sendModifiedDateReport"))) {
            // 小説の最終更新日時一覧をメールで送信
            novelOutputManager.sendModifiedDateReport();
        } else {
            executeFlag = false;
        }

        return executeFlag;
    }

    /**
     * 引数文字列内からURL部分を切り出す.
     *
     * @param arg
     *            引数
     * @return URL
     */
    private String substringUrl(String arg) {
        return arg.substring(arg.indexOf('=') + 1).trim();
    }
}
