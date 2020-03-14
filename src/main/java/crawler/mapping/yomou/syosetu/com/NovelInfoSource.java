package crawler.mapping.yomou.syosetu.com;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import crawler.entity.NovelInfo;
import crawler.exception.NovelNotFoundException;
import lombok.Getter;

/**
 * 小説の付随情報のhtmlを保持するクラス.
 */
@Getter
public class NovelInfoSource extends BaseSource {

    /** 最終更新日時のフォーマット */
    public static final String MODIFIED_DATE_FORMAT = "yyyy年 MM月dd日 HH時mm分";

    /** 小説の付随情報 */
    private final NovelInfo novelInfo;

    /**
     * コンストラクタ.
     *
     * @param add
     *            true:新規、false:更新
     * @param url
     *            小説の付随情報のURL
     * @param novelInfo
     *            小説の付随情報
     * @throws NovelNotFoundException
     *             小説の付随情報が見つからない
     */
    private NovelInfoSource(final boolean add, final String url, final NovelInfo novelInfo) throws NovelNotFoundException {
        super(url, add);
        this.novelInfo = novelInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NovelInfoSource mapping() {
        final LocalDateTime now = LocalDateTime.now();

        if (!add) {
            // 更新の場合、更新日時を変更
            novelInfo.setUpdateDate(now);
        }

        // 小説の付随情報を変更
        novelInfo.setKeyword(NovelElementsUtil.getKeyword(html));
        novelInfo.setModifiedDate(LocalDateTime.parse(NovelElementsUtil.getModifiedDate(html), DateTimeFormatter.ofPattern(MODIFIED_DATE_FORMAT)));
        novelInfo.setFinished(NovelElementsUtil.getFinished(html));
        novelInfo.setCheckedDate(now);
        // 最終更新日時が3年以内の場合、True
        novelInfo.setCheckEnable(novelInfo.getModifiedDate().isAfter(now.minusYears(3)));

        return this;
    }

    /**
     * NovelInfoSourceのインスタンスを生成する.
     *
     * @param url
     *            小説の付随情報のURL
     * @param novelInfo
     *            小説の付随情報
     * @return NovelInfoSourceのインスタンス
     * @throws NovelNotFoundException
     *             指定されたURLが取得出来ない
     */
    public static NovelInfoSource newInstance(final String url, final NovelInfo novelInfo) throws NovelNotFoundException {
        if (novelInfo == null) {
            return new NovelInfoSource(true, url, new NovelInfo()).mapping();
        } else {
            return new NovelInfoSource(false, url, novelInfo).mapping();
        }
    }
}
