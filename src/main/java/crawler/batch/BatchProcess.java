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
     * @throws Exception
     *             例外を標準出力
     */
    public void execute(String[] args) throws Exception;
}
