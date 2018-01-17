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
    public void testExecute() {
        given(messages.getMessage("novelManager.getCheckTargetId")).willReturn("checkForUpdates");
        given(messages.getMessage("novelManager.sendReport")).willReturn("sendReport");
        given(messages.getMessage("novelManager.save")).willReturn("save");
        given(messages.getMessage("novelManager.delete")).willReturn("del");

        novelProcess.execute(new String[] {
                "checkForUpdates",
                "sendReport",
                "save=http://foo.bar",
                "del=http://foo.bar" });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteInvalid() {
        given(messages.getMessage("novelManager.getCheckTargetId")).willReturn("checkForUpdates");
        given(messages.getMessage("novelManager.sendReport")).willReturn("sendReport");
        given(messages.getMessage("novelManager.save")).willReturn("save");
        given(messages.getMessage("novelManager.delete")).willReturn("del");

        // 不正な引数
        novelProcess.execute(new String[] { "test" });
    }
}
