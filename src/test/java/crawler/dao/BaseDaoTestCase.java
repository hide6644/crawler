package crawler.dao;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "classpath:/crawler/dao/applicationContext.xml" })
@Transactional
@Rollback
public abstract class BaseDaoTestCase {

    public static final String PERSISTENCE_UNIT_NAME = "ApplicationEntityManager";

    protected transient Logger log = LogManager.getLogger(this);

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
