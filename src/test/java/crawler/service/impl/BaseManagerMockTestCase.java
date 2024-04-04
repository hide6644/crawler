package crawler.service.impl;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import lombok.extern.log4j.Log4j2;

@ExtendWith(MockitoExtension.class)
@Log4j2
public abstract class BaseManagerMockTestCase {

    protected ResourceBundle rb;

    public BaseManagerMockTestCase() {
        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
            log.trace("No resource bundle found for:{}", className);
        }
    }
}
