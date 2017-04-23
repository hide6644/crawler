package crawler.domain.source;

import static org.junit.Assert.*;

import org.junit.Test;

import crawler.domain.NovelChapter;

public class NovelChapterSourceTest {

    @Test
    public void testIsAdd() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test01.html").getPath();

        NovelChapterSource novelChapterSource = new NovelChapterSource("file://" + filePath);
        novelChapterSource.mapping();
        NovelChapter novelChapter = novelChapterSource.getNovelChapter();
        novelChapter.setTitle("Test小説の章のタイトル");

        assertTrue(novelChapterSource.isAdd());

        novelChapterSource.setNovelChapter(novelChapter);
        novelChapterSource.mapping();

        assertFalse(novelChapterSource.isAdd());
    }
}
