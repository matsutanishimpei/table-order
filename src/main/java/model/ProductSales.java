package model;

import java.io.Serializable;

/**
 * 商品別の売上集計データを保持するレコードです。
 *
 * @param productName 商品名
 * @param totalQuantity 売上合計数量
 * @param totalAmount 売上合計額
 */
public record ProductSales(
        String productName,
        int totalQuantity,
        int totalAmount
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
