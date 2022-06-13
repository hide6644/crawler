package crawler.mapping.yomou.syosetu.com;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class NovelIndexElementTest {

    @Test
    void testEquals() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/20160924/test.html").getPath();

        NovelSource novelSource = NovelSource.newInstance("file://" + filePath);
        List<NovelIndexElement> novelIndexList = novelSource.getNovelIndexList().collect(Collectors.toList());

        assertEquals(novelIndexList.get(0), novelIndexList.get(0));
        assertNotEquals(null, novelIndexList.get(0));

        assertNotEquals(novelIndexList.get(0), novelIndexList.get(1));

        assertEquals(novelIndexList.get(0), novelIndexList.get(0));
    }
}
