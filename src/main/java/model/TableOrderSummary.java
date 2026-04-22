package model;

import java.io.Serializable;
import java.util.List;

/**
 * 座席（テーブル）ごとの注文サマリーデータを保持するモデルクラスです。
 */
public class TableOrderSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private int tableId;
    private String tableName;
    private List<OrderItemView> items;
    private int totalAmount;
    private int orderCount;
    private int unservedCount;

    public TableOrderSummary() {}

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    public List<OrderItemView> getItems() { return items; }
    public void setItems(List<OrderItemView> items) { this.items = items; }
    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }
    public int getOrderCount() { return orderCount; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }
    public int getUnservedCount() { return unservedCount; }
    public void setUnservedCount(int unservedCount) { this.unservedCount = unservedCount; }
}
