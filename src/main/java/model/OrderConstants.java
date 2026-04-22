package model;

/**
 * システム全体で利用する定数クラスです。
 */
public class OrderConstants {
    // ステータス
    public static final int STATUS_ORDERED = 10;     // 注文済み
    public static final int STATUS_COOKING_DONE = 20; // 調理完了
    public static final int STATUS_SERVED = 30;       // 配膳済み
    public static final int STATUS_PAID = 40;         // 会計済み
}
