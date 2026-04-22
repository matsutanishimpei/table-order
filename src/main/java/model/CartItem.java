package model;

import java.io.Serializable;

/**
 * カート内の一つの商品を管理するモデルクラスです。
 */
public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int productId;
    private String name;
    private int unitPrice;
    private int quantity;

    public CartItem() {}

    public CartItem(int productId, String name, int unitPrice, int quantity) {
        this.productId = productId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    /**
     * 小計を計算します。
     * @return 単価 * 数量
     */
    public int getSubtotal() {
        return unitPrice * quantity;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getUnitPrice() { return unitPrice; }
    public void setUnitPrice(int unitPrice) { this.unitPrice = unitPrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
