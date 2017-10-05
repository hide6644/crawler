package crawler;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;

/**
 * ロックファイルを処理する.
 */
public class PreventMultiInstance implements Closeable {

    /** ロックファイルの出力ストリーム */
    private FileOutputStream fos;

    /** ロックファイル */
    private File file;

    /** ファイル領域上のロックオブジェクト */
    private FileLock lock;

    /**
     * デフォルト・コンストラクタ.
     * インスタンス化時にロックファイルを作成する.
     */
    public PreventMultiInstance() {
        this(".lock");
    }

    /**
     * コンストラクタ.
     * インスタンス化時にロックファイルを作成しますが、ロックはまだかけられていません.
     *
     * @param lockfile
     *            ロックファイル名
     */
    public PreventMultiInstance(String lockfile) {
        file = new File(lockfile);
        // ロックファイルを開く.
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            // 何もしない
        }
    }

    /**
     * ファイルをロックする.
     *
     * @return true:ロック取得成功、false:ロック取得失敗
     */
    public boolean tryLock() {
        if (fos == null) {
            return false;
        }

        try {
            // ロックの取得を試行する.
            lock = fos.getChannel().tryLock();

            if (lock != null) {
                // ロックが取得された場合
                return true;
            }
        } catch (OverlappingFileLockException | IOException e) {
            // 何もしない
        }

        // ロックが取れなかった場合
        return false;
    }

    /**
     * ロックされているか.
     *
     * @return true:ロックされている、false:ロックされていない
     */
    public boolean isLocked() {
        return lock != null;
    }

    /**
     * ロックを解除する.
     *
     * @throws IOException
     *             {@link IOException}
     */
    public void release() throws IOException {
        if (lock != null) {
            lock.release();
        }
    }

    /**
     * ロックされていればロックを解除し、 ロックファイルを削除する.
     *
     * @throws IOException
     *             {@link IOException}
     */
    @Override
    public void close() throws IOException {
        fos.close();
        Files.delete(file.toPath());
    }
}
