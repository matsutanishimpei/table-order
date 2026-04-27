package database.impl;

import database.DBManager;
import database.SalesDAO;

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
        String sql = "SELECT SUM(quantity * unit_price) FROM order_items WHERE status = ?";
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
        String sql = "SELECT DATE(updated_at) as sales_date, SUM(quantity * unit_price) as amount, COUNT(DISTINCT o.id) as order_count " +
                     "FROM orders o JOIN order_items oi ON o.id = oi.order_id " +
                     "WHERE o.status = ? " +
                     "GROUP BY sales_date " +
                     "ORDER BY sales_date DESC " +
                     "LIMIT 7";
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
        String sql = "SELECT p.name, SUM(oi.quantity) as total_qty, SUM(oi.quantity * oi.unit_price) as total_amt " +
                     "FROM order_items oi JOIN products p ON oi.product_id = p.id " +
                     "WHERE oi.status = ? " +
                     "GROUP BY p.name " +
                     "ORDER BY total_qty DESC";
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
