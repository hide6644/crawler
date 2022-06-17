package crawler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RunBatchesTest {

    @Test
    void test() throws Exception {
        assertDoesNotThrow(() -> {
            RunBatches.main(new String[] {"test"});
        });
    }
}
