package crawler.service.impl;

import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

import crawler.domain.NovelInfo;
import crawler.service.NovelInfoManager;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

/**
 * 小説の付随情報を管理する.
 */
@Service("novelInfoManager")
public class NovelInfoManagerImpl extends GenericManagerImpl<NovelInfo, Long> implements NovelInfoManager {

    /* (非 Javadoc)
     * 
     * @see crawler.service.NovelInfoManager#saveNovelInfo(net.htmlparser.jericho.Source, crawler.domain.NovelInfo)
     */
    public void saveNovelInfo(final Source html, final NovelInfo novelInfo) {
        Element menuElement = html.getElementById("novel_header");

        for (Element linkElement : menuElement.getAllElements("a")) {
            if (linkElement.getTextExtractor().toString().equals("小説情報")) {
                Source infoHtml = NovelManagerUtil.getSource(NovelManagerUtil.getUrl(linkElement.getAttributeValue("href")));

                novelInfo.setKeyword(NovelElementsUtil.getKeyword(infoHtml));
                novelInfo.setModifiedDate(DateTimeFormat.forPattern("yyyy年 MM月dd日 HH時mm分").parseDateTime(NovelElementsUtil.getModifiedDate(infoHtml)).toDate());
                novelInfo.setFinished(NovelElementsUtil.getFinished(infoHtml));
            }
        }
    }
}
