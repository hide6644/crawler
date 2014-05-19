package crawler.batch;

/**
 * バッチ処理のインターフェイス.
 */
public interface BatchProcess {

    /**
     * バッチ処理.
     */
    public void execute(String[] args) throws Exception;
}
