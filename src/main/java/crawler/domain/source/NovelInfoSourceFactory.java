package crawler.domain.source;

import crawler.domain.NovelInfo;
import crawler.exception.NovelNotFoundException;

/**
 * NovelInfoSourceのインスタンス生成の為のクラス.
 */
public class NovelInfoSourceFactory {

    /**
     * NovelInfoSourceのインスタンスを生成する.
     * 
     * @param url
     *            URL
     * @param novelInfo
     *            小説の付随情報
     * @return NovelInfoSourceのインスタンス
     * @throws NovelNotFoundException
     *             指定されたURLが取得出来ない
     */
    public static NovelInfoSource newInstance(String url, NovelInfo novelInfo) throws NovelNotFoundException {
        NovelInfoSource novelInfoSource = new NovelInfoSource(url);
        novelInfoSource.setNovelInfo(novelInfo);
        novelInfoSource.mapping();

        return novelInfoSource;
    }
}
