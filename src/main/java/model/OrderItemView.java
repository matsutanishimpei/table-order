package model;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注文明細の表示用データを保持するモデルクラス（DTO）です。
 */
@Data
@NoArgsConstructor
public class OrderItemView implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderItemId;
    private String productName;
    private int quantity;
    private String tableName;
    private Timestamp orderedAt;
    private int status;
    private int unitPrice;
}
