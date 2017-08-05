package crawler.domain.source;

import crawler.domain.NovelChapter;
import crawler.exception.NovelNotFoundException;

/**
 * NovelChapterSourceのインスタンス生成の為のクラス.
 */
public class NovelChapterSourceFactory {

    /**
     * NovelChapterSourceのインスタンスを生成する.
     * 
     * @param url
     *            URL
     * @param novelChapter
     *            小説の章の情報
     * @return NovelChapterSourceのインスタンス
     * @throws NovelNotFoundException
     *             指定されたURLが取得出来ない
     */
    public static NovelChapterSource newInstance(String url, NovelChapter novelChapter) throws NovelNotFoundException {
        NovelChapterSource novelChapterSource = new NovelChapterSource(url);
        novelChapterSource.setNovelChapter(novelChapter);
        novelChapterSource.mapping();

        return novelChapterSource;
    }
}
