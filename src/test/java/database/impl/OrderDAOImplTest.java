package database.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.BaseIntegrationTest;
import database.DBManager;
import model.CartItem;
import model.OrderConstants;
import model.OrderItemView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderDAOImplTest extends BaseIntegrationTest {

    private OrderDAOImpl dao;

    @BeforeEach
    void setUp() throws SQLException {
        dao = new OrderDAOImpl();
        // テストごとに注文データをクリア
        try (Connection con = DBManager.getConnection()) {
            con.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
            con.createStatement().execute("DELETE FROM order_items");
            con.createStatement().execute("DELETE FROM orders");
            con.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }

    @Test
    @DisplayName("注文とその明細を登録し、取得できること")
    void testInsertOrderAndItems() throws SQLException {
        try (Connection con = DBManager.getConnection()) {
            con.setAutoCommit(false);
            try {
                // 1. 注文親の登録
                int tableId = 1; // A-1
                int orderId = dao.insertOrder(con, tableId, OrderConstants.STATUS_ORDERED, "test-user");
                assertTrue(orderId > 0);

                // 2. 注文明細の登録
                List<CartItem> items = new ArrayList<>();
                items.add(new CartItem(1, "生ビール", 500, 2)); // Product ID: 1
                items.add(new CartItem(4, "枝豆", 400, 1));    // Product ID: 4
                dao.insertOrderItems(con, orderId, items, OrderConstants.STATUS_ORDERED, "test-user");

                con.commit();

                // 3. 検証: アクティブな注文明細として取得できるか
                List<OrderItemView> activeItems = dao.findActiveOrderItems();
                assertTrue(activeItems.size() >= 2);
                assertTrue(activeItems.stream().anyMatch(i -> i.productName().equals("生ビール")));

                // 4. 検証: テーブルごとの未提供数
                int unservedCount = dao.countUnservedItemsByTable(con, tableId);
                assertEquals(2, unservedCount);

            } finally {
                con.rollback();
            }
        }
    }

    @Test
    @DisplayName("注文明細のステータス更新ができること")
    void testUpdateItemStatus() throws SQLException {
        try (Connection con = DBManager.getConnection()) {
            con.setAutoCommit(false);
            try {
                int orderId = dao.insertOrder(con, 1, OrderConstants.STATUS_ORDERED, "test");
                List<CartItem> items = List.of(new CartItem(1, "p1", 100, 1));
                dao.insertOrderItems(con, orderId, items, OrderConstants.STATUS_ORDERED, "test");
                con.commit();

                // 明細IDを取得
                List<OrderItemView> active = dao.findActiveOrderItems();
                int itemId = active.get(0).orderItemId();

                // ステータス更新: 注文済み -> 調理完了
                boolean success = dao.updateItemStatus(itemId, OrderConstants.STATUS_COOKING_DONE, "kitchen-user");
                assertTrue(success);

                // 検証: 配膳待ち(Ready)リストに含まれるか
                List<OrderItemView> ready = dao.findReadyOrderItems();
                assertTrue(ready.stream().anyMatch(i -> i.orderItemId() == itemId));

                // 検証: ステータスが正しいか
                assertEquals(OrderConstants.STATUS_COOKING_DONE, dao.findItemStatusById(itemId));

            } finally {
                // ロールバック (コミットした分を戻す)
                // 実際には各テスト後に DB をクリーンアップするのが望ましいが、BaseIntegrationTest が毎回 init する前提
            }
        }
    }

    @Test
    @DisplayName("精算用の一括ステータス更新ができること")
    void testUpdateStatusForCheckout() throws SQLException {
        try (Connection con = DBManager.getConnection()) {
            con.setAutoCommit(false);
            try {
                int tableId = 2; // A-2
                int orderId = dao.insertOrder(con, tableId, OrderConstants.STATUS_ORDERED, "test");
                dao.insertOrderItems(con, orderId, List.of(new CartItem(1, "p1", 100, 1)), OrderConstants.STATUS_SERVED, "test");

                // 精算処理: ステータスを「会計済み」に更新
                dao.updateOrderItemsStatusForCheckout(con, tableId, OrderConstants.STATUS_PAID, OrderConstants.STATUS_PAID, "cashier-user");
                dao.updateOrderStatusForCheckout(con, tableId, OrderConstants.STATUS_PAID, OrderConstants.STATUS_PAID, "cashier-user");

                // 検証: アクティブな注文IDが取得できなくなること
                int activeOrderId = dao.findActiveOrderIdByTable(con, tableId);
                assertEquals(-1, activeOrderId);

            } finally {
                con.rollback();
            }
        }
    }
}
