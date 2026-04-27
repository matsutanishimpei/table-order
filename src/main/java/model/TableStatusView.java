package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * テーブル（座席）の現在の状態を表示するためのレコード（DTO）です。
 * 
 * @param tableId テーブルID
 * @param tableName テーブル名
 * @param statusLabel ステータスラベル（空席, 調理待ち等）
 * @param statusCode ステータスコード（CSSクラス用等）
 * @param orderCount 注文数
 * @param totalAmount 合計金額
 * @param lastOrderTime 最終注文日時
 */
public record TableStatusView(
    int tableId,
    String tableName,
    String statusLabel,
    String statusCode,
    int orderCount,
    int totalAmount,
    Timestamp lastOrderTime
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
