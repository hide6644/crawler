package crawler.service;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "classpath:/crawler/dao/applicationContext.xml" })
@Transactional
@Rollback
@Log4j2
public abstract class BaseManagerTestCase {

    protected ResourceBundle rb;

    @Autowired
    protected ApplicationContext applicationContext;

    public BaseManagerTestCase() {
        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
            log.trace("No resource bundle found for:{}", className);
        }
    }
}
