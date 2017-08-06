package crawler.domain.source;

import static org.junit.Assert.*;

import org.junit.Test;

import crawler.domain.NovelChapter;

public class NovelChapterSourceTest {

    @Test
    public void testIsAdd() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test01.html").getPath();

        NovelChapterSource novelChapterSource = NovelChapterSource.newInstance("file://" + filePath, null);
        NovelChapter novelChapter = novelChapterSource.getNovelChapter();

        assertTrue(novelChapterSource.isAdd());

        novelChapter.setTitle("Test小説の章のタイトル");
        novelChapterSource = NovelChapterSource.newInstance("file://" + filePath, novelChapter);

        assertFalse(novelChapterSource.isAdd());
    }
}
