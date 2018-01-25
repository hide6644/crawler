package crawler.batch.impl;

import static org.mockito.BDDMockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.support.MessageSourceAccessor;

import crawler.service.NovelManager;
import crawler.service.NovelOutputManager;

@RunWith(MockitoJUnitRunner.class)
public class NovelProcessTest {

    @Mock
    private MessageSourceAccessor messages;

    @Mock
    private NovelManager novelManager;

    @Mock
    private NovelOutputManager novelOutputManager;

    @InjectMocks
    private NovelProcess novelProcess = new NovelProcess();

    @Test
    public void testExecute() {
        given(messages.getMessage("novelManager.getCheckTargetId")).willReturn("checkForUpdates");
        given(messages.getMessage("novelManager.save")).willReturn("save");
        given(messages.getMessage("novelManager.favorite")).willReturn("fav");
        given(messages.getMessage("novelManager.unfavorite")).willReturn("unfav");
        given(messages.getMessage("novelManager.delete")).willReturn("del");
        given(messages.getMessage("novelOutputManager.sendUnreadReport")).willReturn("sendUnreadReport");
        given(messages.getMessage("novelOutputManager.sendModifiedDateReport")).willReturn("sendModifiedDateReport");

        novelProcess.execute(new String[] {
                "checkForUpdates",
                "save=http://foo.bar",
                "fav=http://foo.bar",
                "unfav=http://foo.bar",
                "del=http://foo.bar",
                "sendUnreadReport",
                "sendModifiedDateReport" });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteInvalid() {
        given(messages.getMessage("novelManager.getCheckTargetId")).willReturn("checkForUpdates");
        given(messages.getMessage("novelManager.save")).willReturn("save");
        given(messages.getMessage("novelManager.favorite")).willReturn("fav");
        given(messages.getMessage("novelManager.unfavorite")).willReturn("unfav");
        given(messages.getMessage("novelManager.delete")).willReturn("del");
        given(messages.getMessage("novelOutputManager.sendUnreadReport")).willReturn("sendUnreadReport");
        given(messages.getMessage("novelOutputManager.sendModifiedDateReport")).willReturn("sendModifiedDateReport");

        // 不正な引数
        novelProcess.execute(new String[] { "test" });
    }
}
