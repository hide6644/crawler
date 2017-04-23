package crawler.exception;

import java.io.FileNotFoundException;

/**
 * 指定された小説が開けなかったことを通知する.
 *
 * @author hide6644
 */
public class NovelNotFoundException extends FileNotFoundException {

    /**
     * デフォルト・コンストラクタ.
     */
    public NovelNotFoundException() {
    }
}
