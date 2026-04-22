package database.impl;

import database.DBManager;
import database.OrderDAO;
import database.BaseDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.CartItem;
import model.OrderConstants;
import model.OrderItemView;
import model.TableOrderSummary;

/**
 * 注文情報のデータベース操作を行うDAO実装クラスです。
 */
public class OrderDAOImpl implements OrderDAO {
    private static final Logger logger = Logger.getLogger(OrderDAOImpl.class.getName());

    /**
     * ResultSet の現在行から OrderItemView オブジェクトを生成します。
     */
    private OrderItemView mapOrderItemRow(ResultSet rs) throws SQLException {
        OrderItemView view = new OrderItemView();
        view.setOrderItemId(rs.getInt("id"));
        view.setProductName(rs.getString("product_name"));
        view.setQuantity(rs.getInt("quantity"));
        view.setTableName(rs.getString("table_name"));
        view.setOrderedAt(rs.getTimestamp("created_at"));
        view.setStatus(rs.getInt("status"));
        return view;
    }

    @Override
    public int insertOrder(Connection con, int tableId, int status) throws SQLException {
        String sqlOrder = "INSERT INTO orders (table_id, status) VALUES (?, ?)";
        try (PreparedStatement psOrder = con.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
            psOrder.setInt(1, tableId);
            psOrder.setInt(2, status);
            psOrder.executeUpdate();

            try (ResultSet rs = psOrder.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    @Override
    public void insertOrderItems(Connection con, int orderId, List<CartItem> cartItems, int status) throws SQLException {
        String sqlItem = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement psItem = con.prepareStatement(sqlItem)) {
            for (CartItem item : cartItems) {
                psItem.setInt(1, orderId);
                psItem.setInt(2, item.getProductId());
                psItem.setInt(3, item.getQuantity());
                psItem.setInt(4, item.getUnitPrice());
                psItem.setInt(5, status);
                psItem.addBatch();
            }
            psItem.executeBatch();
        }
    }

    @Override
    public List<OrderItemView> findActiveOrderItems() {
        List<OrderItemView> list = new ArrayList<>();
        String sql = "SELECT oi.id, p.name as product_name, oi.quantity, st.table_name, oi.created_at, oi.status " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "JOIN orders o ON oi.order_id = o.id " +
                     "JOIN shop_tables st ON o.table_id = st.id " +
                     "WHERE oi.status = ? " +
                     "ORDER BY oi.created_at ASC";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, OrderConstants.STATUS_ORDERED);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapOrderItemRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("キッチン用のアクティブな注文明細の取得中にエラーが発生しました。", e);
        }
        return list;
    }

    @Override
    public boolean updateItemStatus(int itemId, int status) {
        String sql = "UPDATE order_items SET status = ? WHERE id = ?";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("注文明細のステータス更新中にエラーが発生しました。itemId=" + itemId, e);
        }
    }

    @Override
    public List<OrderItemView> findReadyOrderItems() {
        List<OrderItemView> list = new ArrayList<>();
        String sql = "SELECT oi.id, p.name as product_name, oi.quantity, st.table_name, oi.created_at, oi.status " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "JOIN orders o ON oi.order_id = o.id " +
                     "JOIN shop_tables st ON o.table_id = st.id " +
                     "WHERE oi.status = ? " +
                     "ORDER BY oi.created_at ASC";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, OrderConstants.STATUS_COOKING_DONE);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapOrderItemRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("配膳待ち明細の取得中にエラーが発生しました。", e);
        }
        return list;
    }

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
    public void updateOrderItemsStatusForCheckout(Connection con, int tableId, int targetStatus, int conditionStatusLt) throws SQLException {
        String sqlItems = "UPDATE order_items SET status = ? WHERE status < ? AND order_id IN (SELECT id FROM orders WHERE table_id = ?)";
        try (PreparedStatement ps = con.prepareStatement(sqlItems)) {
            ps.setInt(1, targetStatus);
            ps.setInt(2, conditionStatusLt);
            ps.setInt(3, tableId);
            ps.executeUpdate();
        }
    }

    @Override
    public void updateOrderStatusForCheckout(Connection con, int tableId, int targetStatus, int conditionStatusLt) throws SQLException {
        String sqlOrders = "UPDATE orders SET status = ? WHERE status < ? AND table_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlOrders)) {
            ps.setInt(1, targetStatus);
            ps.setInt(2, conditionStatusLt);
            ps.setInt(3, tableId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<model.TableStatusView> findAllTableStatus() {
        List<model.TableStatusView> list = new ArrayList<>();
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
                    model.TableStatusView view = new model.TableStatusView();
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
    public List<model.DailySales> findDailySales() {
        List<model.DailySales> list = new ArrayList<>();
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
                    model.DailySales ds = new model.DailySales();
                    ds.setSalesDate(rs.getDate("sales_date"));
                    ds.setAmount(rs.getInt("amount"));
                    ds.setOrderCount(rs.getInt("order_count"));
                    list.add(ds);
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("日次売上集計の取得中にエラーが発生しました。", e);
        }
        return list;
    }

    @Override
    public List<model.ProductSales> findProductSalesRanking() {
        List<model.ProductSales> list = new ArrayList<>();
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
                    model.ProductSales psModel = new model.ProductSales();
                    psModel.setProductName(rs.getString("name"));
                    psModel.setTotalQuantity(rs.getInt("total_qty"));
                    psModel.setTotalAmount(rs.getInt("total_amt"));
                    list.add(psModel);
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("商品別売上ランキングの取得中にエラーが発生しました。", e);
        }
        return list;
    }
}
