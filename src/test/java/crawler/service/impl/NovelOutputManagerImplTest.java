package crawler.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import crawler.dao.NovelDao;

class NovelOutputManagerImplTest extends BaseManagerMockTestCase {

    @Mock
    private Logger log;

    @Mock
    private NovelDao novelDao;

    @InjectMocks
    private NovelOutputManagerImpl novelOutputManager = new NovelOutputManagerImpl();

    @Test
    void testSendUnreadReport() {
        assertDoesNotThrow(() -> {
            novelOutputManager.sendUnreadReport();
        });
    }

    @Test
    void testSendModifiedDateList() {
        assertDoesNotThrow(() -> {
            novelOutputManager.sendModifiedDateReport();;
        });
    }
}
