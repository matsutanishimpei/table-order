package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 注文明細の表示用データを保持するレコード（DTO）です。
 * 
 * @param orderItemId 注文明細ID
 * @param productName 商品名
 * @param quantity 数量
 * @param tableName テーブル名
 * @param orderedAt 注文日時
 * @param status ステータス
 * @param unitPrice 単価
 */
public record OrderItemView(
    int orderItemId,
    String productName,
    int quantity,
    String tableName,
    Timestamp orderedAt,
    int status,
    int unitPrice
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
