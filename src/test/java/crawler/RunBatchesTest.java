package crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RunBatchesTest {

    @Test
    public void test() throws Exception {
        Assertions.assertDoesNotThrow(() -> {
            RunBatches.main(new String[] {"test"});
        });
    }
}
