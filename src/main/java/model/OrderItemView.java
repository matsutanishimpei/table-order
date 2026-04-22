package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 注文明細の表示用データを保持するモデルクラス（DTO）です。
 */
public class OrderItemView implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderItemId;
    private String productName;
    private int quantity;
    private String tableName;
    private Timestamp orderedAt;
    private int status;
    private int unitPrice;

    public OrderItemView() {}

    public int getOrderItemId() { return orderItemId; }
    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    public Timestamp getOrderedAt() { return orderedAt; }
    public void setOrderedAt(Timestamp orderedAt) { this.orderedAt = orderedAt; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public int getUnitPrice() { return unitPrice; }
    public void setUnitPrice(int unitPrice) { this.unitPrice = unitPrice; }
}
