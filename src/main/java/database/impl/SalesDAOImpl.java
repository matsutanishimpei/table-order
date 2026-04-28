package database.impl;

import database.DBManager;
import database.SalesDAO;
import database.SqlConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        String sql = SqlConstants.SALES_SELECT_TOTAL;
        try (Connection con = DBManager.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, OrderConstants.STATUS_PAID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("累計売上額の取得中にエラーが発生しました。", e);
        }
        return 0;
    }

    @Override
    public List<DailySales> findDailySales() {
        List<DailySales> list = new ArrayList<>();
        String sql = SqlConstants.SALES_SELECT_DAILY;
        try (Connection con = DBManager.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, OrderConstants.STATUS_PAID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DailySales(
                            rs.getDate("sales_date"),
                            rs.getInt("amount"),
                            rs.getInt("order_count")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("日次売上集計の取得中にエラーが発生しました。", e);
        }
        return list;
    }

    @Override
    public List<ProductSales> findProductSalesRanking() {
        List<ProductSales> list = new ArrayList<>();
        String sql = SqlConstants.SALES_SELECT_RANKING;
        try (Connection con = DBManager.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, OrderConstants.STATUS_PAID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ProductSales(
                            rs.getString("name"),
                            rs.getInt("total_qty"),
                            rs.getInt("total_amt")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("商品別売上ランキングの取得中にエラーが発生しました。", e);
        }
        return list;
    }
}
