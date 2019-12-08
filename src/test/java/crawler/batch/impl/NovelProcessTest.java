package crawler.batch.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

import crawler.service.NovelManager;
import crawler.service.NovelOutputManager;

@ExtendWith(MockitoExtension.class)
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
        when(messages.getMessage(anyString())).thenReturn(
                "checkForUpdates", "sendUnreadReport", "sendModifiedDateReport",
                "checkForUpdates", "save", "sendUnreadReport", "sendModifiedDateReport",
                "checkForUpdates", "save", "sendUnreadReport", "sendModifiedDateReport",
                "checkForUpdates", "save", "sendUnreadReport", "sendModifiedDateReport",
                "checkForUpdates", "save", "del", "sendUnreadReport", "sendModifiedDateReport",
                "checkForUpdates", "save", "del", "sendUnreadReport",
                "checkForUpdates", "save", "del", "sendUnreadReport", "sendModifiedDateReport",
                "checkForUpdates", "save", "del", "sendUnreadReport", "sendModifiedDateReport");

        assertDoesNotThrow(() -> {
        novelProcess.execute(new String[] {
                "checkForUpdates",
                "save=http://foo.bar",
                "del=http://foo.bar",
                "sendUnreadReport",
                "sendModifiedDateReport" });
        });
    }

    @Test
    public void testExecuteNull() {
        assertDoesNotThrow(() -> novelProcess.execute(null));
    }

    @Test
    public void testExecuteInvalid() {
        when(messages.getMessage(anyString())).thenReturn("checkForUpdates", "save", "del", "sendUnreadReport", "sendModifiedDateReport");

        // 不正な引数
        assertThrows(IllegalArgumentException.class, () -> {
            novelProcess.execute(new String[] { "test" });
        });
    }
}
