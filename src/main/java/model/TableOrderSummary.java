package model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 座席（テーブル）ごとの注文サマリーデータを保持するモデルクラスです。
 */
@Data
@NoArgsConstructor
public class TableOrderSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private int tableId;
    private String tableName;
    private List<OrderItemView> items;
    private int totalAmount;
    private int orderCount;
    private int unservedCount;
}
