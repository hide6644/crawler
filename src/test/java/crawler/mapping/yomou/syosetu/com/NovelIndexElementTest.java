package crawler.mapping.yomou.syosetu.com;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class NovelIndexElementTest {

    @Test
    public void testEquals() throws Exception {
        String filePath = this.getClass().getClassLoader().getResource("crawler/mapping/yomou/syosetu/com/20160924/test.html").getPath();

        NovelSource novelSource = NovelSource.newInstance("file://" + filePath);
        List<NovelIndexElement> novelIndexList = novelSource.getNovelIndexList().collect(Collectors.toList());

        assertTrue(novelIndexList.get(0).equals(novelIndexList.get(0)));
        assertFalse(novelIndexList.get(0).equals(null));

        assertFalse(novelIndexList.get(0).equals(novelIndexList.get(1)));

        assertTrue(novelIndexList.get(0).equals(novelIndexList.get(0)));
    }
}
