package crawler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import crawler.batch.BatchProcess;

/**
 * 各処理を起動する.
 */
public class RunBatches {

    /** ログ出力クラス */
    private static final Log log = LogFactory.getLog(RunBatches.class);

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
            log.error("[error] ", e);
        }

        context.close();
    }
}
