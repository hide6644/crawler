package crawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import crawler.batch.BatchProcess;

/**
 * 各処理を起動する.
 */
public class RunBatches {

    /** ログ出力クラス */
    private static final Logger log = LogManager.getLogger(RunBatches.class);

    /** Spring設定ファイルの参照先 */
    public static final String CONFIG_LOCATION = "applicationContext.xml";

    /**
     * main処理.
     *
     * @param args
     *            プログラムの引数
     */
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONFIG_LOCATION);

        try (PreventMultiInstance pmi = new PreventMultiInstance();) {
            if (pmi.tryLock()) {
                ((BatchProcess) context.getBean("novelProcess")).execute(args);
            } else {
                // 多重起動
                log.warn("process is running!");
            }
        } catch (Exception e) {
            log.error("Unexpected exception:", e);
        }

        context.close();
    }
}
