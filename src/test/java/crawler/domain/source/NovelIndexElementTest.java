package crawler.domain.source;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class NovelIndexElementTest {

    @Test
    public void testEquals() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();

        NovelSource novelSource = NovelSource.newInstance("file://" + filePath);
        List<NovelIndexElement> novelIndexList = novelSource.getNovelIndexList();

        assertTrue(novelIndexList.get(0).equals(novelIndexList.get(0)));
        assertFalse(novelIndexList.get(0).equals(null));

        assertFalse(novelIndexList.get(0).equals(novelIndexList.get(1)));

        novelIndexList.get(0).setChapterLink(null);
        novelIndexList.get(0).setChapterModifiedDate(null);
        novelIndexList.get(0).setChapterUrl(novelIndexList.get(1).getChapterUrl());

        assertFalse(novelIndexList.get(0).equals(novelIndexList.get(1)));

        novelIndexList.get(0).setChapterLink(novelIndexList.get(1).getChapterLink());

        assertFalse(novelIndexList.get(0).equals(novelIndexList.get(1)));

        novelIndexList.get(0).setChapterLink(null);
        novelIndexList.get(0).setChapterModifiedDate(novelIndexList.get(1).getChapterModifiedDate());

        assertFalse(novelIndexList.get(0).equals(novelIndexList.get(1)));

        novelIndexList.get(0).setChapterLink(novelIndexList.get(1).getChapterLink());

        assertTrue(novelIndexList.get(0).equals(novelIndexList.get(1)));
    }
}
