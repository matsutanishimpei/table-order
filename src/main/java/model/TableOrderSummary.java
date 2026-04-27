package model;

import java.io.Serializable;
import java.util.List;

/**
 * 座席（テーブル）ごとの注文サマリーデータを保持するレコードです。
 * 
 * @param tableId テーブルID
 * @param tableName テーブル名
 * @param items 注文明細リスト
 * @param totalAmount 合計金額
 * @param orderCount 注文数
 * @param unservedCount 未配膳数
 */
public record TableOrderSummary(
    int tableId,
    String tableName,
    List<OrderItemView> items,
    int totalAmount,
    int orderCount,
    int unservedCount
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
