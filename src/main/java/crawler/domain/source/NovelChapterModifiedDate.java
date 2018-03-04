package crawler.domain.source;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import crawler.domain.NovelChapterInfo;

/**
 * 小説の目次に記載されている章の最終更新日時を保持するクラス.
 */
public class NovelChapterModifiedDate {

    /** 最終更新日時のフォーマット */
    public static final String MODIFIED_DATE_FORMAT = "yyyy/MM/dd HH:mm";

    /** 最終更新日時の置き換える文字 */
    public static final String MODIFIED_DATE_REPLACE_REGEX = " 改稿";

    /** 小説の目次に記載されている章の最終更新日時 */
    private String chapterModifiedDate;

    /** 小説の章の付随情報 */
    private NovelChapterInfo novelChapterInfo;

    /**
     * コンストラクタ.
     *
     * @param chapterModifiedDate
     *            小説の目次に記載されている章の最終更新日時
     */
    protected NovelChapterModifiedDate(String chapterModifiedDate) {
        this.chapterModifiedDate = chapterModifiedDate;
    }

    /**
     * 最終更新日時をオブジェクトに変換する.
     */
    protected void mapping() {
        if (novelChapterInfo == null) {
            novelChapterInfo = new NovelChapterInfo();
        } else {
            // 更新の場合
            // 更新日時を変更
            novelChapterInfo.setUpdateDate(LocalDateTime.now());
        }

        // 小説の章の付随情報を変更
        novelChapterInfo.setCheckedDate(LocalDateTime.now());
        novelChapterInfo.setModifiedDate(LocalDateTime.parse(chapterModifiedDate.replaceAll(MODIFIED_DATE_REPLACE_REGEX, ""), DateTimeFormatter.ofPattern(MODIFIED_DATE_FORMAT)));
        novelChapterInfo.setUnread(true);
    }

    /**
     * NovelChapterModifiedDateのインスタンスを生成する.
     *
     * @param novelIndexElement
     *            小説の目次のhtml elementを保持するクラス
     * @param novelChapterInfo
     *            小説の章の付随情報
     * @return NovelChapterModifiedDateのインスタンス
     */
    public static NovelChapterModifiedDate newInstance(NovelIndexElement novelIndexElement, NovelChapterInfo novelChapterInfo) {
        NovelChapterModifiedDate novelChapterModifiedDate = new NovelChapterModifiedDate(novelIndexElement.getChapterModifiedDate());
        novelChapterModifiedDate.setNovelChapterInfo(novelChapterInfo);
        novelChapterModifiedDate.mapping();

        return novelChapterModifiedDate;
    }

    public NovelChapterInfo getNovelChapterInfo() {
        return novelChapterInfo;
    }

    public void setNovelChapterInfo(NovelChapterInfo novelChapterInfo) {
        this.novelChapterInfo = novelChapterInfo;
    }
}
