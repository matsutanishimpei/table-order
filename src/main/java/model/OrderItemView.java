package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * キッチン画面表示用の注文明細データクラスです。
 */
public class OrderItemView implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderItemId;      // 明細ID
    private String productName;   // 商品名
    private int quantity;         // 数量
    private String tableName;     // 座席名
    private Timestamp orderedAt;  // 注文時刻
    private int status;           // ステータス
    private int unitPrice;        // 単価 (会計用)

    // ゲッター/セッター
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

    /**
     * 小計を算出します。
     */
    public int getSubtotal() {
        return this.unitPrice * this.quantity;
    }
}
