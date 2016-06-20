package crawler.service;

import crawler.domain.NovelInfo;
import net.htmlparser.jericho.Source;

/**
 * 小説の付随情報を管理する.
 */
public interface NovelInfoManager extends GenericManager<NovelInfo, Long> {

    /**
     * 小説の付随情報を設定する.
     *
     * @param html
     *            html要素
     * @param novelInfo
     *            小説の付随情報
     */
    public void saveNovelInfo(Source html, NovelInfo novelInfo);
}
