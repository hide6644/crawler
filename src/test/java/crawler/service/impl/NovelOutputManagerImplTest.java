package crawler.service.impl;

import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import crawler.dao.NovelDao;

public class NovelOutputManagerImplTest extends BaseManagerMockTestCase {

    @Mock
    private Logger log;

    @Mock
    private NovelDao novelDao;

    @InjectMocks
    private NovelOutputManagerImpl novelOutputManager = new NovelOutputManagerImpl();

    @Test
    public void testSendReport() {
        novelOutputManager.sendReport();
    }
}
