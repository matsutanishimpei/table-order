package model;

import java.io.Serializable;
import java.util.Date;

/**
 * 監視画面用のテーブル状況を保持するモデルクラスです。
 */
public class TableStatusView implements Serializable {
    private static final long serialVersionUID = 1L;

    private int tableId;
    private String tableName;
    private String statusLabel;   // 空席 / 調理中 / 配膳待ち / 食事中
    private String statusCode;    // idle / cooking / ready / eating
    private int orderCount;
    private int totalAmount;
    private Date lastOrderTime;

    public TableStatusView() {}

    // ゲッター / セッター
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

    public Date getLastOrderTime() { return lastOrderTime; }
    public void setLastOrderTime(Date lastOrderTime) { this.lastOrderTime = lastOrderTime; }
}
