package crawler.domain.source;

import java.net.URL;

import crawler.exception.NovelNotFoundException;
import crawler.util.NovelManagerUtil;
import net.htmlparser.jericho.Source;

/**
 * htmlを保持する基底クラス.
 */
public abstract class BaseSource {

    /** URL */
    protected final URL url;

    /** htmlソース */
    protected final Source html;

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
        this.url = NovelManagerUtil.getUrl(url);
        // URLからhtmlを取得
        this.html = NovelManagerUtil.getSource(this.url);
        this.add = add;
    }

    /**
     * htmlをオブジェクトに変換する.
     */
    protected abstract void mapping();

    /**
     * 新規か、更新か.
     *
     * @return true:新規、false:更新
     */
    public boolean isAdd() {
        return add;
    }

    public URL getUrl() {
        return url;
    }

    public Source getHtml() {
        return html;
    }
}
