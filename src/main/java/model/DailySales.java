package model;

import java.io.Serializable;
import java.sql.Date;

/**
 * 日次の売上集計データを保持するモデルクラスです。
 */
public class DailySales implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date salesDate;
    private int amount;
    private int orderCount;

    public DailySales() {}

    public Date getSalesDate() { return salesDate; }
    public void setSalesDate(Date salesDate) { this.salesDate = salesDate; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public int getOrderCount() { return orderCount; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }
}
