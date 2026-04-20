package model;

/**
 * システム全体で利用する定数を定義します。
 */
public interface OrderConstants {
    // 注文ステータス
    int STATUS_ORDERED = 10;     // 注文中
    int STATUS_COOKING_DONE = 20; // 調理完了
    int STATUS_SERVED = 30;       // 配膳完了
    int STATUS_PAID = 40;         // 会計完了
}
