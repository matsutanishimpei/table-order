package model;

import java.io.Serializable;

/**
 * カート内の1商品を表すレコードです。
 *
 * @param productId 商品ID
 * @param name 商品名
 * @param unitPrice 単価
 * @param quantity 数量
 */
public record CartItem(
        int productId,
        String name,
        int unitPrice,
        int quantity
) implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 初期値付きの引数なしコンストラクタ
     */
    public CartItem() {
        this(0, null, 0, 0);
    }

    /**
     * 小計を計算します。
     *
     * @return 単価 * 数量
     */
    public int getSubtotal() {
        return unitPrice * quantity;
    }
}
