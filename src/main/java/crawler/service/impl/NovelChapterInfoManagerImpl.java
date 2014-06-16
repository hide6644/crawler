package crawler.service.impl;

import net.htmlparser.jericho.Element;

import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

import crawler.domain.NovelChapterInfo;
import crawler.service.NovelChapterInfoManager;

/**
 * 小説の章の付随情報を管理する.
 */
@Service("novelChapterInfoManager")
public class NovelChapterInfoManagerImpl extends GenericManagerImpl<NovelChapterInfo, Long> implements NovelChapterInfoManager {

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.NovelChapterInfoManager#setModifiedDate(crawler.domain.NovelChapterInfo, net.htmlparser.jericho.Element)
     */
    @Override
    public void setModifiedDate(NovelChapterInfo novelChapterInfo, Element chapterElement) {
        Element element = chapterElement.getAllElementsByClass("long_update").get(0);

        if (element.getAllElements("span").size() > 0) {
            novelChapterInfo.setModifiedDate(DateTimeFormat.forPattern("yyyy年 MM月 dd日 改稿").parseDateTime(element.getAllElements("span").get(0).getAttributeValue("title").toString()).toDate());
        } else {
            novelChapterInfo.setModifiedDate(DateTimeFormat.forPattern("yyyy年 MM月 dd日").parseDateTime(element.getTextExtractor().toString()).toDate());
        }
    }
}
