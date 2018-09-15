package crawler.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import crawler.entity.NovelInfo;
import crawler.exception.NovelNotFoundException;
import crawler.util.NovelElementsUtil;

/**
 * 小説の付随情報のhtmlを保持するクラス.
 */
public class NovelInfoSource extends BaseSource {

    /** 最終更新日時のフォーマット */
    public static final String MODIFIED_DATE_FORMAT = "yyyy年 MM月dd日 HH時mm分";

    /** 小説の付随情報 */
    private final NovelInfo novelInfo;

    /**
     * コンストラクタ.
     *
     * @param url
     *            小説の付随情報のURL
     * @param add
     *            true:新規、false:更新
     * @param novelInfo
     *            小説の付随情報
     * @throws NovelNotFoundException
     *             小説の付随情報が見つからない
     */
    private NovelInfoSource(final String url, final boolean add, final NovelInfo novelInfo) throws NovelNotFoundException {
        super(url ,add);
        this.novelInfo = novelInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void mapping() {
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
        // 最終更新日時が1年以内の場合、True
        novelInfo.setCheckEnable(novelInfo.getModifiedDate().isAfter(now.minusYears(1)));
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
    public static NovelInfoSource newInstance(final String url, final NovelInfo novelInfo) throws NovelNotFoundException {
        NovelInfoSource novelInfoSource = null;
        if (novelInfo == null) {
            novelInfoSource = new NovelInfoSource(url, true, new NovelInfo());
        } else {
            novelInfoSource = new NovelInfoSource(url, false, novelInfo);
        }

        novelInfoSource.mapping();

        return novelInfoSource;
    }

    public NovelInfo getNovelInfo() {
        return novelInfo;
    }
}
