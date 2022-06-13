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
import crawler.service.NovelSearchManager;

@ExtendWith(MockitoExtension.class)
class NovelProcessTest {

    @Mock
    private MessageSourceAccessor messages;

    @Mock
    private NovelManager novelManager;

    @Mock
    private NovelOutputManager novelOutputManager;

    @Mock
    private NovelSearchManager novelSearchManager;

    @InjectMocks
    private NovelProcess novelProcess = new NovelProcess();

    @Test
    void testExecute() {
        when(messages.getMessage(anyString())).thenReturn(
                "checkForUpdates", "sendUnreadReport", "sendModifiedDateReport", "reindexAll",
                "checkForUpdates", "save", "sendUnreadReport", "sendModifiedDateReport", "reindexAll",
                "checkForUpdates", "save", "sendUnreadReport", "sendModifiedDateReport", "reindexAll",
                "checkForUpdates", "save", "sendUnreadReport", "sendModifiedDateReport", "reindexAll",
                "checkForUpdates", "save", "del", "sendUnreadReport", "sendModifiedDateReport", "reindexAll",
                "checkForUpdates", "save", "del", "sendUnreadReport", "reindexAll",
                "checkForUpdates", "save", "del", "sendUnreadReport", "sendModifiedDateReport", "reindexAll",
                "checkForUpdates", "save", "del", "sendUnreadReport", "sendModifiedDateReport", "reindexAll");

        assertDoesNotThrow(() -> {
            novelProcess.execute(new String[] {
                    "checkForUpdates",
                    "save=http://foo.bar",
                    "del=http://foo.bar",
                    "sendUnreadReport",
                    "sendModifiedDateReport",
                    "reindexAll" });
        });
    }

    @Test
    void testExecuteNull() {
        assertDoesNotThrow(() -> novelProcess.execute(null));
    }

    @Test
    void testExecuteInvalid() {
        when(messages.getMessage(anyString())).thenReturn("checkForUpdates", "save", "del", "sendUnreadReport", "sendModifiedDateReport", "reindexAll");

        // 不正な引数
        assertThrows(IllegalArgumentException.class, () -> {
            novelProcess.execute(new String[] { "test" });
        });
    }
}
