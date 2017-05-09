package crawler.service.impl;

import org.springframework.stereotype.Service;

import crawler.domain.NovelChapterInfo;
import crawler.domain.source.NovelChapterInfoElement;
import crawler.domain.source.NovelChapterSource;
import crawler.service.NovelChapterInfoManager;
import net.htmlparser.jericho.Element;

/**
 * 小説の章の付随情報を管理する.
 */
@Service("novelChapterInfoManager")
public class NovelChapterInfoManagerImpl extends GenericManagerImpl<NovelChapterInfo, Long> implements NovelChapterInfoManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveNovelChapterInfo(final Element chapterElement, final NovelChapterSource novelChapterSource) {
        NovelChapterInfoElement novelChapterInfoElement = new NovelChapterInfoElement(chapterElement);
        novelChapterInfoElement.setNovelChapterInfo(novelChapterSource.getNovelChapter().getNovelChapterInfo());
        novelChapterInfoElement.mapping();

        novelChapterInfoElement.getNovelChapterInfo().setNovelChapter(novelChapterSource.getNovelChapter());
        novelChapterSource.getNovelChapter().setNovelChapterInfo(novelChapterInfoElement.getNovelChapterInfo());
    }
}
