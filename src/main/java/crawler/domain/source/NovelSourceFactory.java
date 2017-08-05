package crawler.domain.source;

import crawler.domain.Novel;
import crawler.exception.NovelNotFoundException;

/**
 * NovelSourceのインスタンス生成の為のクラス.
 */
public class NovelSourceFactory {

    /**
     * NovelSourceのインスタンスを生成する.
     * 
     * @param url
     *            URL
     * @return NovelSourceのインスタンス
     * @throws NovelNotFoundException
     *             指定されたURLが取得出来ない
     */
    public static NovelSource newInstance(String url) throws NovelNotFoundException {
        return newInstance(url ,null);
    }

    /**
     * NovelSourceのインスタンスを生成する.
     * 
     * @param url
     *            URL
     * @param novel
     *            小説の情報
     * @return NovelSourceのインスタンス
     * @throws NovelNotFoundException
     *             指定されたURLが取得出来ない
     */
    public static NovelSource newInstance(String url, Novel novel) throws NovelNotFoundException {
        NovelSource novelSource = new NovelSource(url);
        novelSource.setNovel(novel);
        novelSource.mapping();

        return novelSource;
    }
}
