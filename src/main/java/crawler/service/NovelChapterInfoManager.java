package crawler.service;

import net.htmlparser.jericho.Element;
import crawler.domain.NovelChapterInfo;

/**
 * 小説の章の付随情報を管理する.
 */
public interface NovelChapterInfoManager extends GenericManager<NovelChapterInfo, Long> {

    /**
     * 小説の章のリンクから更新日付を抽出、小説の章の付随情報に設定する.
     *
     * @param novelChapterInfo
     *            小説の章の付随情報
     * @param chapterElement
     *            小説の章の部分のhtml要素
     */
    public void setModifiedDate(NovelChapterInfo novelChapterInfo, Element chapterElement);
}
