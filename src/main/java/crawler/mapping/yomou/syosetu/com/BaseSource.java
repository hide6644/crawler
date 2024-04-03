package crawler.mapping.yomou.syosetu.com;

import org.jsoup.nodes.Document;

import crawler.exception.NovelNotFoundException;
import crawler.util.NovelManagerUtil;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * htmlを保持する基底クラス.
 */
@Getter
@Log4j2
public abstract class BaseSource {

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
