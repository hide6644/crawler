package crawler.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.search.query.facet.Facet;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import crawler.domain.NovelInfo;

public class NovelInfoDaoTest extends BaseDaoTestCase {

    @Autowired
    private NovelInfoDao dao;

    @Test
    public void testSearch() throws Exception {
        dao.reindex();

        List<NovelInfo> novelInfoList = dao.search("ファンタジー");
        assertNotNull(novelInfoList);
        novelInfoList = dao.search(new String[]{"ファンタジー"}, new String[]{"keyword"});
        assertNotNull(novelInfoList);
    }

    @Test
    public void testFacet() throws Exception {
        dao.reindex();

        List<Facet> facet = dao.facet("keywordSet.keyword", 10);
        assertNotNull(facet);
    }
}
