package crawler.service;

import net.htmlparser.jericho.Source;
import crawler.domain.NovelInfo;

/**
 * 小説の付随情報を管理する.
 */
public interface NovelInfoManager extends GenericManager<NovelInfo, Long> {

    /**
     * 小説の付随情報を設定する.
     *
     * @param novelInfo
     *            小説の付随情報
     * @param html
     *            html要素
     */
    public void setNovelInfo(NovelInfo novelInfo, Source html);
}
