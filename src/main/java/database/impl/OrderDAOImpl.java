package database.impl;

import database.DBManager;
import database.SqlConstants;
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
        return new OrderItemView(
            rs.getInt("id"),
            rs.getString("product_name"),
            rs.getInt("quantity"),
            rs.getString("table_name"),
            rs.getTimestamp("created_at"),
            rs.getInt("status"),
            rs.getInt("unit_price")
        );
    }

    @Override
    public int insertOrder(Connection con, int tableId, int status) throws SQLException {
        String sqlOrder = SqlConstants.ORDER_INSERT;
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
        String sqlItem = SqlConstants.ORDER_ITEM_INSERT;
        try (PreparedStatement psItem = con.prepareStatement(sqlItem)) {
            cartItems.forEach(item -> {
                try {
                    psItem.setInt(1, orderId);
                    psItem.setInt(2, item.productId());
                    psItem.setInt(3, item.quantity());
                    psItem.setInt(4, item.unitPrice());
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
        String sql = SqlConstants.ORDER_ITEM_SELECT_ACTIVE;

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
        String sql = SqlConstants.ORDER_ITEM_UPDATE_STATUS;
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
        String sql = SqlConstants.ORDER_ITEM_SELECT_ACTIVE;

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
        String sqlItems = SqlConstants.ORDER_ITEMS_UPDATE_STATUS_FOR_CHECKOUT;
        try (PreparedStatement ps = con.prepareStatement(sqlItems)) {
            ps.setInt(1, targetStatus);
            ps.setInt(2, conditionStatusLt);
            ps.setInt(3, tableId);
            ps.executeUpdate();
        }
    }

    @Override
    public void updateOrderStatusForCheckout(Connection con, int tableId, int targetStatus, int conditionStatusLt) throws SQLException {
        String sqlOrders = SqlConstants.ORDER_UPDATE_STATUS_FOR_CHECKOUT;
        try (PreparedStatement ps = con.prepareStatement(sqlOrders)) {
            ps.setInt(1, targetStatus);
            ps.setInt(2, conditionStatusLt);
            ps.setInt(3, tableId);
            ps.executeUpdate();
        }
    }
}
