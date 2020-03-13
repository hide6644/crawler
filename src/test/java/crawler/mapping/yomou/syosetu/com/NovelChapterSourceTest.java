package crawler.mapping.yomou.syosetu.com;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import crawler.entity.NovelChapter;
import crawler.mapping.yomou.syosetu.com.NovelChapterSource;

public class NovelChapterSourceTest {

    @Test
    public void testIsAdd() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/20160924/test01.html").getPath();

        NovelChapterSource novelChapterSource = NovelChapterSource.newInstance("file://" + filePath, null);
        NovelChapter novelChapter = novelChapterSource.getNovelChapter();

        assertTrue(novelChapterSource.isAdd());

        novelChapter.setTitle("Test小説の章のタイトル");
        novelChapterSource = NovelChapterSource.newInstance("file://" + filePath, novelChapter);

        assertFalse(novelChapterSource.isAdd());
    }
}
