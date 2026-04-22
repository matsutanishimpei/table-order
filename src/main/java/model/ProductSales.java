package model;

import java.io.Serializable;

/**
 * 商品別の売上集計データを保持するモデルクラスです。
 */
public class ProductSales implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productName;
    private int totalQuantity;
    private int totalAmount;

    public ProductSales() {}

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }
}
