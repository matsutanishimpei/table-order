package model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品別の売上集計データを保持するモデルクラスです。
 */
@Data
@NoArgsConstructor
public class ProductSales implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productName;
    private int totalQuantity;
    private int totalAmount;
}
