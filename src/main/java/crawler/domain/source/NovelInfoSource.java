package crawler.domain.source;

import java.util.Date;

import org.joda.time.format.DateTimeFormat;

import crawler.domain.NovelInfo;
import crawler.exception.NovelNotFoundException;
import crawler.util.NovelElementsUtil;
import crawler.util.NovelManagerUtil;

/**
 * 小説の付随情報のhtmlを保持するクラス.
 */
public class NovelInfoSource extends BaseSource {

    /** 最終更新日時のフォーマット */
    public static final String MODIFIED_DATE_FORMAT = "yyyy年 MM月dd日 HH時mm分";

    /** 小説の付随情報 */
    private NovelInfo novelInfo;

    /**
     * コンストラクタ.
     *
     * @param url
     *            小説の付随情報のURL
     * @throws NovelNotFoundException
     *             小説の付随情報が見つからない
     */
    protected NovelInfoSource(String url) throws NovelNotFoundException {
        this.url = NovelManagerUtil.getUrl(url);
        // URLからhtmlを取得
        html = NovelManagerUtil.getSource(this.url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void mapping() {
        if (novelInfo == null) {
            novelInfo = new NovelInfo();
        } else {
            // 更新の場合
            // 更新日時を変更
            novelInfo.setUpdateDate(new Date());
        }

        // 小説の付随情報を変更
        novelInfo.setKeyword(NovelElementsUtil.getKeyword(html));
        novelInfo.setModifiedDate(DateTimeFormat.forPattern(MODIFIED_DATE_FORMAT)
                .parseDateTime(NovelElementsUtil.getModifiedDate(html)).toDate());
        novelInfo.setFinished(NovelElementsUtil.getFinished(html));
        novelInfo.setCheckedDate(new Date());
    }

    /**
     * NovelInfoSourceのインスタンスを生成する.
     *
     * @param url
     *            URL
     * @param novelInfo
     *            小説の付随情報
     * @return NovelInfoSourceのインスタンス
     * @throws NovelNotFoundException
     *             指定されたURLが取得出来ない
     */
    public static NovelInfoSource newInstance(String url, NovelInfo novelInfo) throws NovelNotFoundException {
        NovelInfoSource novelInfoSource = new NovelInfoSource(url);
        novelInfoSource.setNovelInfo(novelInfo);
        novelInfoSource.mapping();

        return novelInfoSource;
    }

    public NovelInfo getNovelInfo() {
        return novelInfo;
    }

    public void setNovelInfo(NovelInfo novelInfo) {
        this.novelInfo = novelInfo;
    }
}
