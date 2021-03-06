package crawler.mapping.yomou.syosetu.com;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;

import crawler.exception.NovelNotFoundException;
import crawler.util.NovelManagerUtil;
import lombok.Getter;

/**
 * htmlを保持する基底クラス.
 */
@Getter
public abstract class BaseSource {

    /** ログ出力クラス */
    protected Logger log = LogManager.getLogger(this);

    /** htmlソース */
    protected final Document html;

    /** 新規フラグ */
    protected final boolean add;

    /**
     * コンストラクタ.
     *
     * @param url
     *            URL
     * @param add
     *            true:新規、false:更新
     * @throws NovelNotFoundException
     *             URLが見つからない
     */
    protected BaseSource(String url, boolean add) throws NovelNotFoundException {
        log.debug("[open] url:{}", url);
        // URLからhtmlを取得
        this.html = NovelManagerUtil.getSource(url);
        log.debug("[close] url:{}", url);
        this.add = add;
    }

    /**
     * htmlをオブジェクトに変換する.
     *
     * @return htmlを保持するオブジェクト
     */
    protected abstract BaseSource mapping();
}
