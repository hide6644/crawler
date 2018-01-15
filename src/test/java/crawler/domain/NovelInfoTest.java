package crawler.domain;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

public class NovelInfoTest {

    @Test
    public void testNeedsCheckForUpdate() {
        // 完結済み小説 => 更新チェック対象
        Novel novel = new Novel();
        NovelInfo novelInfo = new NovelInfo();
        novelInfo.setFinished(true);
        novelInfo.setCheckedDate(DateTime.now().minusDays(45).toDate());
        novelInfo.setNovel(novel);

        assertTrue(novelInfo.needsCheckForUpdate());

        // 完結済み小説 => 更新チェック非対象
        novelInfo.setCheckedDate(DateTime.now().minusDays(44).toDate());

        assertFalse(novelInfo.needsCheckForUpdate());

        // 最終更新日が30以上前 => 更新チェック対象
        novelInfo.setFinished(false);
        novelInfo.setCheckedDate(DateTime.now().minusDays(15).toDate());
        novelInfo.setModifiedDate(DateTime.now().minusDays(30).toDate());

        assertTrue(novelInfo.needsCheckForUpdate());

        // 最終更新日が30以上前 => 更新チェック非対象
        novelInfo.setCheckedDate(DateTime.now().minusDays(14).toDate());

        assertFalse(novelInfo.needsCheckForUpdate());

        // 更新チェック対象
        novelInfo.setCheckedDate(DateTime.now().minusDays(5).toDate());
        novelInfo.setModifiedDate(DateTime.now().minusDays(10).toDate());

        assertTrue(novelInfo.needsCheckForUpdate());

        // 更新チェック非対象
        novelInfo.setCheckedDate(DateTime.now().minusDays(4).toDate());

        assertFalse(novelInfo.needsCheckForUpdate());
    }
}
