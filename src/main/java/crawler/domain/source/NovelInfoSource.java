package crawler.domain.source;

import java.net.URL;
import java.util.Date;

import org.joda.time.format.DateTimeFormat;

import crawler.domain.NovelInfo;
import crawler.util.NovelElementsUtil;
import crawler.util.NovelManagerUtil;
import net.htmlparser.jericho.Source;

/**
 * 小説の付随情報のhtml
 */
public class NovelInfoSource {

    /** 小説の付随情報のURL */
    private URL url;

    /** 小説の付随情報のhtml */
    private Source html;

    /** 小説の付随情報 */
    private NovelInfo novelInfo;

    /**
     * コンストラクタ.
     *
     * @param url
     *            小説の付随情報のURL
     */
    public NovelInfoSource(String url) {
        this.url = NovelManagerUtil.getUrl(url);
        // URLからhtmlを取得
        html = NovelManagerUtil.getSource(this.url);

        if (html == null) {
            throw new NullPointerException();
        }
    }

    /**
     * コンストラクタ.
     *
     * @param url
     *            小説の付随情報のURL
     * @param html
     *            小説の付随情報のhtml
     */
    public NovelInfoSource(URL url, Source html) {
        if (url == null || html == null) {
            throw new NullPointerException();
        }
        this.url = url;
        this.html = html;
    }

    /**
     * 小説の付随情報のhtmlを小説の付随情報(NovelInfo)に変換する.
     */
    public void mapping() {
        if (novelInfo == null) {
            novelInfo = new NovelInfo();
        } else {
            // 更新の場合
            novelInfo.setUpdateDate(new Date());
        }

        novelInfo.setKeyword(NovelElementsUtil.getKeyword(html));
        novelInfo.setModifiedDate(DateTimeFormat.forPattern("yyyy年 MM月dd日 HH時mm分").parseDateTime(NovelElementsUtil.getModifiedDate(html)).toDate());
        novelInfo.setFinished(NovelElementsUtil.getFinished(html));
        novelInfo.setCheckedDate(new Date());
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

    public NovelInfo getNovelInfo() {
        return novelInfo;
    }

    public void setNovelInfo(NovelInfo novelInfo) {
        this.novelInfo = novelInfo;
    }
}
