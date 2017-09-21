package crawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import crawler.batch.BatchProcess;
import net.sf.ehcache.CacheManager;

/**
 * 各処理を起動する.
 */
public class RunBatches {

    /** ログ出力クラス */
    private static final Logger log = LogManager.getLogger(RunBatches.class);

    /**
     * main処理.
     *
     * @param args
     *            プログラムの引数
     */
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        try (PreventMultiInstance pmi = new PreventMultiInstance();) {
            if (!pmi.tryLock()) {
                // 多重起動
                log.warn("process is running!");
            } else {
                ((BatchProcess) context.getBean("novelProcess")).execute(args);
            }
        } catch (Exception e) {
            log.error("[error] ", e);
        }

        CacheManager.getInstance().shutdown();
        context.close();
    }
}
