package model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * カート内の1商品を表すモデルクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int productId;
    private String name;
    private int unitPrice;
    private int quantity;

    /**
     * 小計を計算します。
     * @return 単価 * 数量
     */
    public int getSubtotal() {
        return unitPrice * quantity;
    }
}
