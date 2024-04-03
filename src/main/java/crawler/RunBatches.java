package crawler;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import crawler.batch.BatchProcess;
import lombok.extern.log4j.Log4j2;

/**
 * 各処理を起動する.
 */
@Log4j2
public class RunBatches {

    /** Spring設定ファイルの参照先 */
    public static final String CONFIG_LOCATION = "applicationContext.xml";

    /**
     * main処理.
     *
     * @param args
     *            プログラムの引数
     */
    public static void main(String[] args) {
        var context = new ClassPathXmlApplicationContext(CONFIG_LOCATION);

        try (var pmi = new PreventMultiInstance();) {
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
