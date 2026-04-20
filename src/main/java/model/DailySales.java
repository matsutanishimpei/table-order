package model;

import java.io.Serializable;
import java.util.Date;

/**
 * 日次売上データを保持するモデルクラスです。
 */
public class DailySales implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Date salesDate;
    private int amount;
    private int orderCount;

    public DailySales() {}

    public DailySales(Date salesDate, int amount, int orderCount) {
        this.salesDate = salesDate;
        this.amount = amount;
        this.orderCount = orderCount;
    }

    public Date getSalesDate() { return salesDate; }
    public void setSalesDate(Date salesDate) { this.salesDate = salesDate; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public int getOrderCount() { return orderCount; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }
}
