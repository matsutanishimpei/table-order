package model;

import java.io.Serializable;
import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 日次の売上集計データを保持するモデルクラスです。
 */
@Data
@NoArgsConstructor
public class DailySales implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date salesDate;
    private int amount;
    private int orderCount;
}
