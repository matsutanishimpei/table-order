package model;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * テーブル（座席）の現在の状態を表示するためのモデルクラス（DTO）です。
 */
@Data
@NoArgsConstructor
public class TableStatusView implements Serializable {
    private static final long serialVersionUID = 1L;

    private int tableId;
    private String tableName;
    private String statusLabel; // 空席, 調理待ち, 配膳待ち, 食事中
    private String statusCode;  // CSSクラス用など
    private int orderCount;
    private int totalAmount;
    private Timestamp lastOrderTime;
}
