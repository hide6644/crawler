package crawler.domain.source;

import crawler.domain.NovelChapterInfo;

/**
 * NovelChapterModifiedDateのインスタンス生成の為のクラス.
 */
public class NovelChapterModifiedDateFactory {

    /**
     * NovelChapterModifiedDateのインスタンスを生成する.
     * 
     * @param novelIndexElement
     *            小説の目次のhtml elementを保持するクラス
     * @param novelChapterInfo
     *            小説の章の付随情報
     * @return NovelChapterModifiedDateのインスタンス
     */
    public static NovelChapterModifiedDate newInstance(NovelIndexElement novelIndexElement, NovelChapterInfo novelChapterInfo) {
        NovelChapterModifiedDate novelChapterModifiedDate = new NovelChapterModifiedDate(novelIndexElement.getChapterModifiedDate());
        novelChapterModifiedDate.setNovelChapterInfo(novelChapterInfo);
        novelChapterModifiedDate.mapping();

        return novelChapterModifiedDate;
    }
}
