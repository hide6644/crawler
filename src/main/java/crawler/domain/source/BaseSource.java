package crawler.domain.source;

import java.net.URL;

import net.htmlparser.jericho.Source;

/**
 * htmlを保持する基底クラス.
 */
public abstract class BaseSource {

    /** URL */
    protected URL url;

    /** htmlソース */
    protected Source html;

    /** 新規フラグ */
    protected boolean add;

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

    public void setUrl(URL url) {
        this.url = url;
    }

    public Source getHtml() {
        return html;
    }

    public void setHtml(Source html) {
        this.html = html;
    }
}
