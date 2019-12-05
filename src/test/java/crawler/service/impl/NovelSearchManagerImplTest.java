package crawler.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import crawler.dao.HibernateSearch;

public class NovelSearchManagerImplTest extends BaseManagerMockTestCase {

    @Mock
    private HibernateSearch novelSearch;

    @InjectMocks
    private NovelSearchManagerImpl novelSearchManager = new NovelSearchManagerImpl();

    @Test
    public void testReindexAll() {
        assertDoesNotThrow(() -> {
            novelSearchManager.reindexAll(true);
        });
    }
}
