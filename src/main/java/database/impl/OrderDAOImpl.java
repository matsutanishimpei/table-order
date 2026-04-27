package database.impl;

import database.DBManager;
import database.OrderDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.CartItem;
import model.OrderConstants;
import model.OrderItemView;

/**
 * 注文情報のデータベース操作を行うDAO実装クラスです。
 */
public class OrderDAOImpl implements OrderDAO {

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
        view.setUnitPrice(rs.getInt("unit_price"));
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
            cartItems.forEach(item -> {
                try {
                    psItem.setInt(1, orderId);
                    psItem.setInt(2, item.getProductId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setInt(4, item.getUnitPrice());
                    psItem.setInt(5, status);
                    psItem.addBatch();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            psItem.executeBatch();
        }
    }

    @Override
    public List<OrderItemView> findActiveOrderItems() {
        List<OrderItemView> list = new ArrayList<>();
        String sql = "SELECT oi.id, p.name as product_name, oi.quantity, st.table_name, oi.created_at, oi.status, oi.unit_price " +
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
        String sql = "SELECT oi.id, p.name as product_name, oi.quantity, st.table_name, oi.created_at, oi.status, oi.unit_price " +
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
}
