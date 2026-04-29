package database.impl;

import database.JdbcExecutor;
import database.SalesDAO;
import database.SqlConstants;

import java.util.List;

import model.DailySales;
import model.OrderConstants;
import model.ProductSales;

/**
 * 売上集計に関するデータベース操作を行うDAO実装クラスです。
 */
public class SalesDAOImpl implements SalesDAO {

    @Override
    public int getTotalSales() {
        return JdbcExecutor.queryOne(SqlConstants.SALES_SELECT_TOTAL,
                rs -> rs.getInt(1), OrderConstants.STATUS_PAID).orElse(0);
    }

    @Override
    public List<DailySales> findDailySales() {
        return JdbcExecutor.query(SqlConstants.SALES_SELECT_DAILY,
                rs -> new DailySales(
                        rs.getDate("sales_date"),
                        rs.getInt("amount"),
                        rs.getInt("order_count")
                ), OrderConstants.STATUS_PAID);
    }

    @Override
    public List<ProductSales> findProductSalesRanking() {
        return JdbcExecutor.query(SqlConstants.SALES_SELECT_RANKING,
                rs -> new ProductSales(
                        rs.getString("name"),
                        rs.getInt("total_qty"),
                        rs.getInt("total_amt")
                ), OrderConstants.STATUS_PAID);
    }
}
