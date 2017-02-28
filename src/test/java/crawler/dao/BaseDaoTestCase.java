package crawler.dao;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:/crawler/dao/applicationContext.xml" })
public abstract class BaseDaoTestCase extends AbstractTransactionalJUnit4SpringContextTests {

    public static final String PERSISTENCE_UNIT_NAME = "ApplicationEntityManager";

    protected transient Logger log = LogManager.getLogger(getClass());

    protected ResourceBundle rb;

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    protected EntityManager entityManager;

    public BaseDaoTestCase() {
        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
            log.trace("No resource bundle found for: " + className);
        }
    }
}
