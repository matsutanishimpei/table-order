package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * テーブル（座席）の現在の状態を表示するためのモデルクラス（DTO）です。
 */
public class TableStatusView implements Serializable {
    private static final long serialVersionUID = 1L;

    private int tableId;
    private String tableName;
    private String statusLabel; // 空席, 調理待ち, 配膳待ち, 食事中
    private String statusCode;  // CSSクラス用など
    private int orderCount;
    private int totalAmount;
    private Timestamp lastOrderTime;

    public TableStatusView() {}

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }
    public String getStatusCode() { return statusCode; }
    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }
    public int getOrderCount() { return orderCount; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }
    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }
    public Timestamp getLastOrderTime() { return lastOrderTime; }
    public void setLastOrderTime(Timestamp lastOrderTime) { this.lastOrderTime = lastOrderTime; }
}
