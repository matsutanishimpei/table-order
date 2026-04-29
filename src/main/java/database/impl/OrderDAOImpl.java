package database.impl;

import database.DBManager;
import database.JdbcExecutor;
import database.RowMapper;
import database.SqlConstants;
import database.OrderDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

import model.CartItem;
import model.OrderConstants;
import model.OrderItemView;

/**
 * 注文情報のデータベース操作を行うDAO実装クラスです。
 */
@Slf4j
public class OrderDAOImpl implements OrderDAO {

    private final RowMapper<OrderItemView> mapper = rs -> new OrderItemView(
            rs.getInt("id"),
            rs.getString("product_name"),
            rs.getInt("quantity"),
            rs.getString("table_name"),
            rs.getTimestamp("created_at"),
            rs.getInt("status"),
            rs.getInt("unit_price")
    );

    @Override
    public int insertOrder(Connection con, int tableId, int status, String operatorId) throws SQLException {
        return JdbcExecutor.insertAndReturnId(con, SqlConstants.ORDER_INSERT, tableId, status, operatorId);
    }

    @Override
    public void insertOrderItems(Connection con, int orderId, List<CartItem> cartItems,
            int status, String operatorId) throws SQLException {
        List<Object[]> batchParams = cartItems.stream()
                .map(item -> new Object[]{orderId, item.productId(), item.quantity(), item.unitPrice(), status, operatorId})
                .collect(Collectors.toList());
        JdbcExecutor.batchUpdate(con, SqlConstants.ORDER_ITEM_INSERT, batchParams);
    }

    @Override
    public List<OrderItemView> findActiveOrderItems() {
        return JdbcExecutor.query(SqlConstants.ORDER_ITEM_SELECT_ACTIVE, mapper, OrderConstants.STATUS_ORDERED);
    }

    @Override
    public boolean updateItemStatus(int itemId, int status, String operatorId) {
        return JdbcExecutor.update(SqlConstants.ORDER_ITEM_UPDATE_STATUS, status, operatorId, itemId) > 0;
    }

    @Override
    public List<OrderItemView> findReadyOrderItems() {
        return JdbcExecutor.query(SqlConstants.ORDER_ITEM_SELECT_ACTIVE, mapper, OrderConstants.STATUS_COOKING_DONE);
    }

    @Override
    public void updateOrderItemsStatusForCheckout(Connection con, int tableId, int targetStatus,
            int conditionStatusLt, String operatorId) throws SQLException {
        JdbcExecutor.update(con, SqlConstants.ORDER_ITEMS_UPDATE_STATUS_FOR_CHECKOUT,
                targetStatus, operatorId, conditionStatusLt, tableId);
    }

    @Override
    public void updateOrderStatusForCheckout(Connection con, int tableId, int targetStatus,
            int conditionStatusLt, String operatorId) throws SQLException {
        JdbcExecutor.update(con, SqlConstants.ORDER_UPDATE_STATUS_FOR_CHECKOUT,
                targetStatus, operatorId, conditionStatusLt, tableId);
    }

    @Override
    public int countUnservedItemsByTable(Connection con, int tableId) throws SQLException {
        Optional<Integer> count = JdbcExecutor.queryOne(con, SqlConstants.ORDER_ITEM_COUNT_UNSERVED,
                rs -> rs.getInt(1), tableId, OrderConstants.STATUS_SERVED);
        return count.orElse(0);
    }

    @Override
    public int findItemStatusById(int itemId) {
        return JdbcExecutor.queryOne(SqlConstants.ORDER_ITEM_SELECT_STATUS, rs -> rs.getInt("status"), itemId)
                .orElse(-1);
    }

    @Override
    public int findActiveOrderIdByTable(Connection con, int tableId) throws SQLException {
        return JdbcExecutor.queryOne(con, SqlConstants.ORDER_SELECT_ACTIVE_ID,
                rs -> rs.getInt("id"), tableId, OrderConstants.STATUS_PAID)
                .orElse(-1);
    }
}
