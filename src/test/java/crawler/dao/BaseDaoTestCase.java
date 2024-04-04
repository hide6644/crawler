package crawler.dao;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.extension.ExtendWith;
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
public abstract class BaseDaoTestCase {

    public static final String PERSISTENCE_UNIT_NAME = "ApplicationEntityManager";

    protected ResourceBundle rb;

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    protected EntityManager entityManager;

    public BaseDaoTestCase() {
        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
            log.trace("No resource bundle found for:{}", className);
        }
    }
}
