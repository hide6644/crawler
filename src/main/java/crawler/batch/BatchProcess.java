package crawler.batch;

/**
 * バッチ処理のインターフェイス.
 */
public interface BatchProcess {

    /**
     * バッチ処理.
     *
     * @param args
     *            引数
     */
    public void execute(String[] args);
}
