package crawler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PreventMultiInstanceTest {

    @Test
    public void testLock() throws Exception {
        PreventMultiInstance pmi = new PreventMultiInstance();
        pmi.release();

        assertFalse(pmi.isLocked());
        assertTrue(pmi.tryLock());
        assertFalse(pmi.tryLock());
        assertTrue(pmi.isLocked());
        pmi.release();
        assertFalse(pmi.isLocked());

        pmi.close();
    }

    @Test
    public void testNotFoundLockFile() throws Exception {
        PreventMultiInstance pmi = new PreventMultiInstance("./test/.lock");

        assertFalse(pmi.tryLock());

        pmi.close();
    }
 }
