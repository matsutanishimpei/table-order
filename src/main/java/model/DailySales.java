package model;

import java.io.Serializable;
import java.sql.Date;

/**
 * 日次の売上集計データを保持するレコードです。
 * 
 * @param salesDate 売上日
 * @param amount 売上合計額
 * @param orderCount 注文数
 */
public record DailySales(Date salesDate, int amount, int orderCount) implements Serializable {
    private static final long serialVersionUID = 1L;
}
