package crawler.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class NovelInfoTest {

    @Test
    public void testNeedsCheckForUpdate() {
        // 完結済み小説 => 更新チェック対象
        Novel novel = new Novel();
        NovelInfo novelInfo = new NovelInfo();
        novelInfo.setFinished(true);
        novelInfo.setCheckedDate(LocalDateTime.now().minusDays(45));
        novelInfo.setModifiedDate(LocalDateTime.now());
        novelInfo.setNovel(novel);

        assertTrue(novelInfo.needsCheckForUpdate());

        // 完結済み小説 => 更新チェック非対象
        novelInfo.setCheckedDate(LocalDateTime.now().minusDays(44));

        assertFalse(novelInfo.needsCheckForUpdate());

        // 最終更新日が30以上前 => 更新チェック対象
        novelInfo.setFinished(false);
        novelInfo.setCheckedDate(LocalDateTime.now().minusDays(15));
        novelInfo.setModifiedDate(LocalDateTime.now().minusDays(30));

        assertTrue(novelInfo.needsCheckForUpdate());

        // 最終更新日が30以上前 => 更新チェック非対象
        novelInfo.setCheckedDate(LocalDateTime.now().minusDays(14));

        assertFalse(novelInfo.needsCheckForUpdate());

        // 更新チェック対象
        novelInfo.setCheckedDate(LocalDateTime.now().minusDays(5));
        novelInfo.setModifiedDate(LocalDateTime.now().minusDays(10));

        assertTrue(novelInfo.needsCheckForUpdate());

        // 更新チェック非対象
        novelInfo.setCheckedDate(LocalDateTime.now().minusDays(4));

        assertFalse(novelInfo.needsCheckForUpdate());
    }
}
