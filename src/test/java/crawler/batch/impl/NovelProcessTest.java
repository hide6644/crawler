package crawler.batch.impl;

import static org.mockito.BDDMockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.support.MessageSourceAccessor;

import crawler.service.NovelManager;

@RunWith(MockitoJUnitRunner.class)
public class NovelProcessTest {

    @Mock
    private MessageSourceAccessor messages;

    @Mock
    private NovelManager novelManager;

    @InjectMocks
    private NovelProcess novelProcess = new NovelProcess();

    @Test
    public void testExecute() throws Exception {
        given(messages.getMessage("novelManager.getCheckTargetId")).willReturn("checkForUpdates");
        given(messages.getMessage("novelManager.sendReport")).willReturn("sendReport");

        novelProcess.execute(new String[] {
                "checkForUpdates",
                "sendReport",
                "http://foo.bar" });
    }
}
