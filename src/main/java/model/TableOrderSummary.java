package model;

import java.io.Serializable;
import java.util.List;

/**
 * 特定のテーブルの会計情報を集約するクラスです。
 */
public class TableOrderSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private int tableId;
    private String tableName;
    private int totalAmount;
    private int orderCount;
    private List<OrderItemView> items;

    // ゲッター/セッター
    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }

    public int getOrderCount() { return orderCount; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }

    public List<OrderItemView> getItems() { return items; }
    public void setItems(List<OrderItemView> items) { this.items = items; }
}
