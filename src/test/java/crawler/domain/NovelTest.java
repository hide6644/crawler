package crawler.domain;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

public class NovelTest {

    @Test
    public void testNeedsCheckForUpdate() {
        // 完結済み小説 => 更新チェック対象
        Novel novel = new Novel();
        NovelInfo novelInfo = new NovelInfo();
        novelInfo.setFinished(true);
        novelInfo.setCheckedDate(DateTime.now().minusDays(45).toDate());
        novel.setNovelInfo(novelInfo);

        assertTrue(novel.needsCheckForUpdate());

        // 完結済み小説 => 更新チェック非対象
        novelInfo.setCheckedDate(DateTime.now().minusDays(44).toDate());
        novel.setNovelInfo(novelInfo);

        assertFalse(novel.needsCheckForUpdate());

        // 最終更新日が30以上前 => 更新チェック対象
        novelInfo.setFinished(false);
        novelInfo.setCheckedDate(DateTime.now().minusDays(15).toDate());
        novelInfo.setModifiedDate(DateTime.now().minusDays(30).toDate());
        novel.setNovelInfo(novelInfo);

        assertTrue(novel.needsCheckForUpdate());

        // 最終更新日が30以上前 => 更新チェック非対象
        novelInfo.setCheckedDate(DateTime.now().minusDays(14).toDate());
        novel.setNovelInfo(novelInfo);

        assertFalse(novel.needsCheckForUpdate());

        // 更新チェック対象
        novelInfo.setCheckedDate(DateTime.now().minusDays(5).toDate());
        novelInfo.setModifiedDate(DateTime.now().minusDays(10).toDate());
        novel.setNovelInfo(novelInfo);

        assertTrue(novel.needsCheckForUpdate());

        // 更新チェック非対象
        novelInfo.setCheckedDate(DateTime.now().minusDays(4).toDate());
        novel.setNovelInfo(novelInfo);

        assertFalse(novel.needsCheckForUpdate());
    }

    @Test
    public void testEquals() {
        Novel novel1 = new Novel();
        novel1.setUrl("test1");

        assertTrue(novel1.equals(novel1));
        assertFalse(novel1.equals(null));

        Novel novel2 = new Novel();

        assertFalse(novel1.equals(novel2));

        novel2.setUrl("test2");

        assertFalse(novel1.equals(novel2));

        novel2.setUrl("test1");

        assertTrue(novel1.equals(novel2));
    }
}
