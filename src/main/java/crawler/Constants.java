package crawler;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

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

    /** Resource Bundleの名前 */
    public static final String BUNDLE_KEY = "applicationResources";

    /** Resource Bundle */
    public static final ResourceBundle RB = ResourceBundle.getBundle(BUNDLE_KEY);

    /** ファイル区切り文字 */
    public static final String FILE_SEP = System.getProperty("file.separator");

    /** 実行フォルダ名 */
    public static final String APP_FOLDER_NAME = System.getProperty("app_home") + Constants.FILE_SEP;

    /** ローカルファイルの接頭辞 */
    public static final String LOCAL_FILE_PREFIX = RB.getString("local.file.prefix");

    /** デフォルトエンコード */
    public static final Charset ENCODING = StandardCharsets.UTF_8;

    /** Entity Managerクラス名 */
    public static final String PERSISTENCE_UNIT_NAME = "ApplicationEntityManager";

    /** 一時停止時間 */
    public static final long DELAY_ACCESS_TIME = Long.parseLong(RB.getString("delay.access.time")) * 1000;

    /** プロキシホスト */
    public static final String PROXY_HOST = RB.getString("proxyHost");

    /** プロキシポート番号(デフォルト:8080) */
    public static final int PROXY_PORT = Integer.parseInt(StringUtils.isNotEmpty(RB.getString("proxyPort")) ? RB.getString("proxyPort") : "8080");

    /** プロキシ認証ユーザー */
    public static final String PROXY_USER = RB.getString("proxyUser");

    /** プロキシ認証パスワード */
    public static final String PROXY_PASS = RB.getString("proxyPass");
}
