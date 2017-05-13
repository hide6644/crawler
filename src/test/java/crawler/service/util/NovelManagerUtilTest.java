package crawler.service.util;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import crawler.domain.Novel;
import crawler.domain.NovelInfo;
import crawler.util.NovelManagerUtil;

public class NovelManagerUtilTest {

    @Test
    public void testIsConfirmedNovel() throws Exception {
        // 完結済み小説 => 更新チェック対象
        Novel novel = new Novel();
        NovelInfo novelInfo = new NovelInfo();
        novelInfo.setFinished(true);
        novelInfo.setCheckedDate(DateTime.now().minusDays(45).toDate());
        novel.setNovelInfo(novelInfo);

        assertTrue(NovelManagerUtil.isConfirmedNovel(novel));

        // 完結済み小説 => 更新チェック非対象
        novelInfo.setCheckedDate(DateTime.now().minusDays(44).toDate());
        novel.setNovelInfo(novelInfo);

        assertFalse(NovelManagerUtil.isConfirmedNovel(novel));

        // 最終更新日が30以上前 => 更新チェック対象
        novelInfo.setFinished(false);
        novelInfo.setCheckedDate(DateTime.now().minusDays(15).toDate());
        novelInfo.setModifiedDate(DateTime.now().minusDays(30).toDate());
        novel.setNovelInfo(novelInfo);

        assertTrue(NovelManagerUtil.isConfirmedNovel(novel));

        // 最終更新日が30以上前 => 更新チェック非対象
        novelInfo.setCheckedDate(DateTime.now().minusDays(14).toDate());
        novel.setNovelInfo(novelInfo);

        assertFalse(NovelManagerUtil.isConfirmedNovel(novel));

        // 更新チェック対象
        novelInfo.setCheckedDate(DateTime.now().minusDays(5).toDate());
        novelInfo.setModifiedDate(DateTime.now().minusDays(10).toDate());
        novel.setNovelInfo(novelInfo);

        assertTrue(NovelManagerUtil.isConfirmedNovel(novel));

        // 更新チェック非対象
        novelInfo.setCheckedDate(DateTime.now().minusDays(4).toDate());
        novel.setNovelInfo(novelInfo);

        assertFalse(NovelManagerUtil.isConfirmedNovel(novel));
    }
}
