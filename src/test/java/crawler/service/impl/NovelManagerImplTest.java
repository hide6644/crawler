package crawler.service.impl;

import java.io.File;
import java.net.URL;

import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import crawler.dao.NovelDao;
import crawler.domain.source.NovelSource;
import crawler.service.NovelChapterManager;
import crawler.service.NovelInfoManager;
import net.htmlparser.jericho.Source;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ NovelManagerImpl.class })
@PowerMockIgnore("javax.management.*")
public class NovelManagerImplTest extends BaseManagerMockTestCase {

    @Mock
    private NovelSource novelSource;

    @Mock
    private Logger log;

    @Mock
    private NovelDao novelDao;

    @Mock
    private NovelInfoManager novelInfoManager;

    @Mock
    private NovelChapterManager novelChapterManager;

    @InjectMocks
    private NovelManagerImpl novelManager;

    @Test
    public void testGetCheckTargetId() throws Exception {
        File file = new File("./test.html");
        URL url = new URL("http://www.foo.bar/test.html");
        NovelSource novelSource = new NovelSource(url, new Source(file));

        {
            MockitoAnnotations.initMocks(this);
            PowerMockito.whenNew(NovelSource.class).withArguments(url).thenReturn(novelSource);
        }

        novelManager.add("http://www.foo.bar/test.html");
    }
}
