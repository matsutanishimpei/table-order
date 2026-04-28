package database.impl;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import database.BaseIntegrationTest;
import database.DBManager;
import model.DailySales;
import model.OrderConstants;

/**
 * SalesDAOImpl の集計ロジックに関する統合テストです。
 * ステータスによる除外や日付境界の挙動を検証します。
 */
public class SalesDAOImplIntegrationTest extends BaseIntegrationTest {

    private SalesDAOImpl salesDAO;

    @BeforeEach
    void setUp() throws Exception {
        salesDAO = new SalesDAOImpl();
        // テストごとにデータをクリア
        try (Connection con = DBManager.getConnection()) {
            con.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
            con.createStatement().execute("DELETE FROM order_items");
            con.createStatement().execute("DELETE FROM orders");
            con.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }

    @Test
    @DisplayName("集計の正確性: PAIDステータスのみが集計され、他は除外されること")
    void findDailySales_ExcludesNonPaidOrders() throws Exception {
        // Arrange: 1つは PAID、もう1つは ORDERED
        insertTestOrder(1, OrderConstants.STATUS_PAID, 1000, "2026-04-28 10:00:00");
        insertTestOrder(2, OrderConstants.STATUS_ORDERED, 2000, "2026-04-28 11:00:00");

        // Act
        List<DailySales> result = salesDAO.findDailySales();

        // Assert
        assertEquals(1, result.size(), "PAIDのみが集計対象になるべき");
        assertEquals(1000, result.get(0).amount());
    }

    @Test
    @DisplayName("日付の境界値: 23:59:59 と 00:00:01 が別の日として集計されること")
    void findDailySales_DateBoundary() throws Exception {
        // Arrange: 日付を跨ぐ2つの注文
        insertTestOrder(1, OrderConstants.STATUS_PAID, 1000, "2026-04-27 23:59:59");
        insertTestOrder(2, OrderConstants.STATUS_PAID, 2000, "2026-04-28 00:00:01");

        // Act
        List<DailySales> result = salesDAO.findDailySales();

        // Assert
        assertEquals(2, result.size(), "2日分に分かれるべき");
        // 最新の日付が先頭 (ORDER BY sales_date DESC)
        assertEquals(2000, result.get(0).amount(), "28日の売上");
        assertEquals(1000, result.get(1).amount(), "27日の売上");
    }

    private void insertTestOrder(int id, int status, int price, String timestamp) throws Exception {
        try (Connection con = DBManager.getConnection()) {
            String orderSql = "INSERT INTO orders (id, table_id, status, created_at, updated_at) VALUES (?, 1, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(orderSql)) {
                ps.setInt(1, id);
                ps.setInt(2, status);
                ps.setString(3, timestamp);
                ps.setString(4, timestamp);
                ps.executeUpdate();
            }
            String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, status, created_at) VALUES (?, 1, 1, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(itemSql)) {
                ps.setInt(1, id);
                ps.setInt(2, price);
                ps.setInt(3, status);
                ps.setString(4, timestamp);
                ps.executeUpdate();
            }
        }
    }
}
