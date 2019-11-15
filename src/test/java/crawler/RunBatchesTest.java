package crawler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class RunBatchesTest {

    @Test
    public void test() throws Exception {
        assertDoesNotThrow(() -> {
            RunBatches.main(new String[] {"test"});
        });
    }
}
