package crawler.service.impl;

import net.htmlparser.jericho.Element;

import java.util.Date;

import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

import crawler.domain.NovelChapter;
import crawler.domain.NovelChapterInfo;
import crawler.service.NovelChapterInfoManager;

/**
 * 小説の章の付随情報を管理する.
 */
@Service("novelChapterInfoManager")
public class NovelChapterInfoManagerImpl extends GenericManagerImpl<NovelChapterInfo, Long> implements NovelChapterInfoManager {

    /* (非 Javadoc)
     *
     * @see crawler.service.NovelChapterInfoManager#saveNovelChapterInfo(net.htmlparser.jericho.Element, crawler.domain.NovelChapter)
     */
    @Override
    public NovelChapterInfo saveNovelChapterInfo(final Element chapterElement, final NovelChapter novelChapter) {
        NovelChapterInfo novelChapterInfo = novelChapter.getNovelChapterInfo();

        if (novelChapterInfo == null) {
            novelChapterInfo = new NovelChapterInfo();
        } else {
            novelChapterInfo.setUpdateDate(new Date());
        }

        novelChapterInfo.setCheckedDate(new Date());
        novelChapterInfo.setModifiedDate(DateTimeFormat.forPattern("yyyy年 MM月 dd日").parseDateTime(NovelElementsUtil.getChapterModifiedDate(chapterElement, true).replaceAll(" 改稿", "")).toDate());
        novelChapterInfo.setUnread(true);

        return novelChapterInfo;
    }
}
