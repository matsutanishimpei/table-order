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

    /**
     * 防御的コピーを行うコンストラクタです。
     */
    public TableOrderSummary(int tableId, String tableName, List<OrderItemView> items,
                             int totalAmount, int orderCount, int unservedCount) {
        this.tableId = tableId;
        this.tableName = tableName;
        this.items = items != null ? List.copyOf(items) : null;
        this.totalAmount = totalAmount;
        this.orderCount = orderCount;
        this.unservedCount = unservedCount;
    }

    @Override
    public List<OrderItemView> items() {
        return items != null ? List.copyOf(items) : null;
    }
}
