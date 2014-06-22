package crawler.service.impl;

import java.io.IOException;
import java.net.URL;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

import crawler.domain.NovelInfo;
import crawler.service.NovelInfoManager;

/**
 * 小説の章の情報を管理する.
 */
@Service("novelInfoManager")
public class NovelInfoManagerImpl extends GenericManagerImpl<NovelInfo, Long> implements NovelInfoManager {

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelInfoManager#setNovelInfo(crawler.domain.NovelInfo, net.htmlparser.jericho.Source)
     */
    @Override
    public void setNovelInfo(NovelInfo novelInfo, Source html) {
        Element menuElement = html.getElementById("novel_header");

        for (Element linkElement : menuElement.getAllElements("a")) {
            if (linkElement.getTextExtractor().toString().equals("小説情報")) {
                try {
                    Source infoHtml = new Source(new URL(linkElement.getAttributeValue("href")));
                    infoHtml.fullSequentialParse();
                    String keyword = infoHtml.getElementById("noveltable1").getAllElements("td").get(2).getTextExtractor().toString();
                    String modifiedDate = infoHtml.getElementById("noveltable2").getAllElements("td").get(1).getTextExtractor().toString();
                    Element finishedElement = infoHtml.getElementById("noveltype");

                    novelInfo.setKeyword(keyword);
                    novelInfo.setModifiedDate(DateTimeFormat.forPattern("yyyy年 MM月dd日 HH時mm分").parseDateTime(modifiedDate).toDate());

                    if (finishedElement != null && finishedElement.getTextExtractor().toString().equals("完結済")) {
                        novelInfo.setFinished(true);
                    } else {
                        novelInfo.setFinished(false);
                    }
                } catch (IOException e) {
                    log.error("[error] url:" + linkElement.getAttributeValue("href"), e);
                    continue;
                }
            }
        }
    }
}
