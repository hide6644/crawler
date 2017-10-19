package crawler;

import java.io.File;
import java.util.ResourceBundle;

/**
 * システム全体の定数クラス.
 */
public class Constants {

    /**
     * プライベート・コンストラクタ.
     * このクラスはインスタンス化禁止.
     */
    private Constants() {
    }

    /** ResourceBundleの名前 */
    public static final String BUNDLE_KEY = "applicationResources";

    /** ファイル区切り文字 */
    public static final String FILE_SEP = File.separator;

    /** 実行フォルダ名 */
    public static final String APP_FOLDER_NAME = System.getProperty("app_home") + Constants.FILE_SEP;

    /** 一時停止時間 */
    public static final long DELAY_ACCESS_TIME = Long.parseLong(ResourceBundle.getBundle(BUNDLE_KEY).getString("delay.access.time")) * 1000;
}
