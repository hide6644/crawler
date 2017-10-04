package crawler;

import static org.junit.Assert.*;

import org.junit.Test;

public class PreventMultiInstanceTest {

    @Test
    public void test() throws Exception {
        PreventMultiInstance pmi = new PreventMultiInstance();

        assertFalse(pmi.isLocked());
        assertTrue(pmi.tryLock());
        assertFalse(pmi.tryLock());
        assertTrue(pmi.isLocked());

        pmi.close();
    }
}
