package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 注文明細の表示用データを保持するレコード（DTO）です。
 *
 * @param orderItemId 注文明細ID
 * @param productName 商品名
 * @param quantity 数量
 * @param tableName テーブル名
 * @param orderedAt 注文日時
 * @param status ステータス
 * @param unitPrice 単価
 */
public record OrderItemView(
        int orderItemId,
        String productName,
        int quantity,
        String tableName,
        Timestamp orderedAt,
        int status,
        int unitPrice
) implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 防御的コピーを行うコンストラクタです。
     */
    public OrderItemView(int orderItemId, String productName, int quantity, String tableName,
                         Timestamp orderedAt, int status, int unitPrice) {
        this.orderItemId = orderItemId;
        this.productName = productName;
        this.quantity = quantity;
        this.tableName = tableName;
        this.orderedAt = orderedAt != null ? new Timestamp(orderedAt.getTime()) : null;
        this.status = status;
        this.unitPrice = unitPrice;
    }

    @Override
    public Timestamp orderedAt() {
        return orderedAt != null ? new Timestamp(orderedAt.getTime()) : null;
    }
}
