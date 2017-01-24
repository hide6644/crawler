package crawler.domain.source;

import java.util.Date;

import org.joda.time.format.DateTimeFormat;

import crawler.domain.NovelInfo;
import crawler.util.NovelElementsUtil;
import crawler.util.NovelManagerUtil;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

/**
 * 小説の付随情報のhtml
 */
public class NovelInfoSource {

    /** 小説のhtml */
    private Source html;

    /** 小説の付随情報 */
    private NovelInfo novelInfo;

    /**
     * コンストラクタ.
     *
     * @param html
     *            小説のhtml
     */
    public NovelInfoSource(Source html) {
        if (html == null) {
            throw new NullPointerException();
        }
        this.html = html;
    }

    /**
     * 小説のhtmlから小説の付随情報のhtmlを取得し、小説の付随情報(NovelInfo)に変換する.
     */
    public void mapping() {
        if (novelInfo == null) {
            novelInfo = new NovelInfo();
        } else {
            // 更新の場合
            novelInfo.setUpdateDate(new Date());
        }

        Element menuElement = html.getElementById("novel_header");

        for (Element linkElement : menuElement.getAllElements("a")) {
            if (linkElement.getTextExtractor().toString().equals("小説情報")) {
                Source infoHtml = NovelManagerUtil.getSource(NovelManagerUtil.getUrl(linkElement.getAttributeValue("href")));

                novelInfo.setKeyword(NovelElementsUtil.getKeyword(infoHtml));
                novelInfo.setModifiedDate(DateTimeFormat.forPattern("yyyy年 MM月dd日 HH時mm分").parseDateTime(NovelElementsUtil.getModifiedDate(infoHtml)).toDate());
                novelInfo.setFinished(NovelElementsUtil.getFinished(infoHtml));
            }
        }

        novelInfo.setCheckedDate(new Date());
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
