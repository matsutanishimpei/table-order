package database.impl;

import database.DBManager;
import database.TableDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.OrderConstants;
import model.OrderItemView;
import model.TableOrderSummary;
import model.TableStatusView;

/**
 * 座席（テーブル）状態に関するデータベース操作を行うDAO実装クラスです。
 */
public class TableDAOImpl implements TableDAO {

    @Override
    public List<TableOrderSummary> findUnsettledTables() {
        List<TableOrderSummary> list = new ArrayList<>();
        String sql = "SELECT st.id, st.table_name, COUNT(oi.id) as item_count, SUM(oi.quantity * oi.unit_price) as total_amt, " +
                     "SUM(CASE WHEN oi.status < ? THEN 1 ELSE 0 END) as unserved_count " +
                     "FROM shop_tables st " +
                     "JOIN orders o ON st.id = o.table_id " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "WHERE oi.status < ? " +
                     "GROUP BY st.id, st.table_name " +
                     "ORDER BY st.id";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, OrderConstants.STATUS_SERVED);
            ps.setInt(2, OrderConstants.STATUS_PAID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TableOrderSummary summary = new TableOrderSummary();
                    summary.setTableId(rs.getInt("id"));
                    summary.setTableName(rs.getString("table_name"));
                    summary.setOrderCount(rs.getInt("item_count"));
                    summary.setUnservedCount(rs.getInt("unserved_count"));
                    summary.setTotalAmount(rs.getInt("total_amt"));
                    list.add(summary);
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("未精算テーブル一覧の取得中にエラーが発生しました。", e);
        }
        return list;
    }

    @Override
    public TableOrderSummary getTableOrderSummary(int tableId) {
        TableOrderSummary summary = new TableOrderSummary();
        summary.setTableId(tableId);

        String sqlTable = "SELECT table_name FROM shop_tables WHERE id = ?";
        String sqlItems = "SELECT oi.id, p.name, oi.quantity, oi.unit_price, oi.status " +
                         "FROM order_items oi " +
                         "JOIN products p ON oi.product_id = p.id " +
                         "JOIN orders o ON oi.order_id = o.id " +
                         "WHERE o.table_id = ? AND oi.status < ? " +
                         "ORDER BY oi.created_at";

        try (Connection con = DBManager.getConnection()) {
            try (PreparedStatement psTable = con.prepareStatement(sqlTable)) {
                psTable.setInt(1, tableId);
                try (ResultSet rsTable = psTable.executeQuery()) {
                    if (rsTable.next()) {
                        summary.setTableName(rsTable.getString("table_name"));
                    }
                }
            }

            List<OrderItemView> items = new ArrayList<>();
            int total = 0;
            try (PreparedStatement psItems = con.prepareStatement(sqlItems)) {
                psItems.setInt(1, tableId);
                psItems.setInt(2, OrderConstants.STATUS_PAID);
                try (ResultSet rsItems = psItems.executeQuery()) {
                    while (rsItems.next()) {
                        OrderItemView item = new OrderItemView();
                        item.setOrderItemId(rsItems.getInt("id"));
                        item.setProductName(rsItems.getString("name"));
                        item.setQuantity(rsItems.getInt("quantity"));
                        item.setUnitPrice(rsItems.getInt("unit_price"));
                        item.setStatus(rsItems.getInt("status"));
                        items.add(item);
                        total += item.getQuantity() * item.getUnitPrice();
                    }
                }
            }
            summary.setItems(items);
            summary.setTotalAmount(total);
        } catch (SQLException e) {
            throw new exception.DatabaseException("座席別注文サマリーの取得中にエラーが発生しました。tableId=" + tableId, e);
        }
        return summary;
    }

    @Override
    public List<TableStatusView> findAllTableStatus() {
        List<TableStatusView> list = new ArrayList<>();
        String sql = "SELECT st.id, st.table_name, MIN(oi.status) as min_status, " +
                     "COUNT(oi.id) as item_count, SUM(oi.quantity * oi.unit_price) as total_amt, MAX(oi.created_at) as last_order " +
                     "FROM shop_tables st " +
                     "LEFT JOIN orders o ON st.id = o.table_id AND o.status < ? " +
                     "LEFT JOIN order_items oi ON o.id = oi.order_id AND oi.status < ? " +
                     "GROUP BY st.id, st.table_name ORDER BY st.id";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, OrderConstants.STATUS_PAID);
            ps.setInt(2, OrderConstants.STATUS_PAID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TableStatusView view = new TableStatusView();
                    view.setTableId(rs.getInt("id"));
                    view.setTableName(rs.getString("table_name"));
                    
                    int itemCount = rs.getInt("item_count");
                    if (itemCount == 0) {
                        view.setStatusLabel("空席");
                        view.setStatusCode("0"); 
                    } else {
                        Object minStatusObj = rs.getObject("min_status");
                        if (minStatusObj == null) {
                            view.setStatusLabel("空席");
                            view.setStatusCode("0");
                        } else {
                            int minStatus = (Integer)minStatusObj;
                            if (minStatus == OrderConstants.STATUS_ORDERED) {
                                view.setStatusLabel("調理待ち");
                                view.setStatusCode("10");
                            } else if (minStatus == OrderConstants.STATUS_COOKING_DONE) {
                                view.setStatusLabel("配膳待ち");
                                view.setStatusCode("20");
                            } else if (minStatus == OrderConstants.STATUS_SERVED) {
                                view.setStatusLabel("食事中");
                                view.setStatusCode("30");
                            } else {
                                view.setStatusLabel("空席");
                                view.setStatusCode("0");
                            }
                        }
                    }
                    
                    view.setOrderCount(itemCount);
                    view.setTotalAmount(rs.getInt("total_amt"));
                    view.setLastOrderTime(rs.getTimestamp("last_order"));
                    list.add(view);
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("全テーブルのステータス取得中にエラーが発生しました。", e);
        }
        return list;
    }
}
