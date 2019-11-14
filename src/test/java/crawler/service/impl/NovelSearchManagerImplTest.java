package crawler.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import crawler.dao.HibernateSearch;
import crawler.entity.Novel;

public class NovelSearchManagerImplTest extends BaseManagerMockTestCase {

    @Mock
    private HibernateSearch<Novel> novelSearch;

    @InjectMocks
    private NovelSearchManagerImpl novelSearchManager = new NovelSearchManagerImpl();

    @Test
    public void testReindexAll() {
        Assertions.assertDoesNotThrow(() -> {
            novelSearchManager.reindexAll(true);
        });
    }
}
