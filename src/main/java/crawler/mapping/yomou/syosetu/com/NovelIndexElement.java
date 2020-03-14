package crawler.mapping.yomou.syosetu.com;

import org.jsoup.nodes.Element;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 小説の目次のhtml elementを保持するクラス.
 */
@Getter
@EqualsAndHashCode
public class NovelIndexElement {

    /** 小説の目次に記載されている章のURL */
    @EqualsAndHashCode.Exclude
    private final String chapterUrl;

    /** 小説の目次に記載されている章のリンク */
    private final String chapterLink;

    /** 小説の目次に記載されている章の最終更新日時 */
    private final String chapterModifiedDate;

    /**
     * コンストラクタ.
     *
     * @param element
     *            小説の目次のhtml element
     */
    public NovelIndexElement(final Element element) {
        chapterUrl = NovelElementsUtil.getChapterUrlByNovelBody(element);
        chapterLink = NovelElementsUtil.getChapterTitleByNovelBody(element);
        chapterModifiedDate = NovelElementsUtil.getChapterModifiedDate(element);
    }
}
