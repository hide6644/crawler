package crawler.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import crawler.domain.NovelInfo;

public class NovelInfoDaoTest extends BaseDaoTestCase {

    @Autowired
    private NovelInfoDao dao;

    @Test
    public void testSearchNovelInfoByKeyword() throws Exception {
        List<NovelInfo> novelInfoList = dao.searchNovelInfoByKeyword("TEST");

        assertNotNull(novelInfoList);
    }
}
