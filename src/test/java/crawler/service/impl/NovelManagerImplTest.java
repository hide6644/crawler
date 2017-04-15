package crawler.service.impl;

import static org.mockito.BDDMockito.*;

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
import crawler.domain.Novel;
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
    private NovelManagerImpl novelManager = new NovelManagerImpl();

    @Test
    public void testAdd() throws Exception {
        String fileName = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();
        File file = new File(fileName);
        String url = "http://www.foo.bar/20160924/";
        NovelSource novelSource = new NovelSource(new URL(url), new Source(file));

        {
            MockitoAnnotations.initMocks(this);
            PowerMockito.whenNew(NovelSource.class).withArguments(url).thenReturn(novelSource);
        }

        novelManager.add(url);
    }

    @Test
    public void testCheckForUpdatesAndSaveHistory() throws Exception {
        String fileName = this.getClass().getClassLoader().getResource("novel/20160924/test.html").getPath();
        File file = new File(fileName);
        String url = "http://www.foo.bar/20160924/";
        NovelSource novelSource = new NovelSource(new URL(url), new Source(file));

        {
            MockitoAnnotations.initMocks(this);
            PowerMockito.whenNew(NovelSource.class).withArguments(url).thenReturn(novelSource);
        }

        Novel novel = new Novel();
        novel.setUrl(url);

        given(novelDao.get(1L)).willReturn(novel);

        novelManager.checkForUpdatesAndSaveHistory(1L);
        novelManager.checkForUpdatesAndSaveHistory(2L);
    }
}
