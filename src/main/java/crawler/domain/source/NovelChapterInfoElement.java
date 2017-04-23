package crawler.domain.source;

import java.util.Date;

import org.joda.time.format.DateTimeFormat;

import crawler.domain.NovelChapterInfo;
import crawler.exception.NovelNotFoundException;
import crawler.util.NovelElementsUtil;
import net.htmlparser.jericho.Element;

/**
 * 小説の章の付随情報のhtml
 */
public class NovelChapterInfoElement {

    /** 小説の章の付随情報のhtml */
    private Element element;

    /** 小説の章の付随情報 */
    private NovelChapterInfo novelChapterInfo;

    /**
     * コンストラクタ.
     *
     * @param element
     *            小説の章の付随情報のhtml
     * @throws NovelNotFoundException
     *             小説の章の付随情報が見つからない
     */
    public NovelChapterInfoElement(Element element) throws NovelNotFoundException {
        if (element == null) {
            throw new NovelNotFoundException();
        }
        this.element = element;
    }

    /**
     * 小説の章の付随情報のhtmlを小説の章の付随情報(NovelChapterInfo)に変換する.
     */
    public void mapping() {
        if (novelChapterInfo == null) {
            novelChapterInfo = new NovelChapterInfo();
        } else {
            // 更新の場合
            novelChapterInfo.setUpdateDate(new Date());
        }

        novelChapterInfo.setCheckedDate(new Date());
        novelChapterInfo.setModifiedDate(DateTimeFormat.forPattern("yyyy/MM/dd HH:mm").parseDateTime(NovelElementsUtil.getChapterModifiedDate(element, true).replaceAll(" 改稿", "")).toDate());
        novelChapterInfo.setUnread(true);
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public NovelChapterInfo getNovelChapterInfo() {
        return novelChapterInfo;
    }

    public void setNovelChapterInfo(NovelChapterInfo novelChapterInfo) {
        this.novelChapterInfo = novelChapterInfo;
    }
}
