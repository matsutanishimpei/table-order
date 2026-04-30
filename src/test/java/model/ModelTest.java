package model;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void testUserRoles() {
        User admin = new User("a", "p", UserConstants.ROLE_ADMIN, null, false);
        assertTrue(admin.isAdmin());
        assertTrue(admin.isKitchen());
        assertTrue(admin.isHall());
        assertTrue(admin.isCashier());
        assertFalse(admin.isTableTerminal());

        User kitchen = new User("k", "p", UserConstants.ROLE_KITCHEN, null, false);
        assertFalse(kitchen.isAdmin());
        assertTrue(kitchen.isKitchen());
        assertFalse(kitchen.isHall());
        assertFalse(kitchen.isCashier());

        User hall = new User("h", "p", UserConstants.ROLE_HALL, null, false);
        assertFalse(hall.isAdmin());
        assertFalse(hall.isKitchen());
        assertTrue(hall.isHall());
        assertFalse(hall.isCashier());

        User cashier = new User("c", "p", UserConstants.ROLE_CASHIER, null, false);
        assertFalse(cashier.isAdmin());
        assertFalse(cashier.isKitchen());
        assertFalse(cashier.isHall());
        assertTrue(cashier.isCashier());

        User table = new User("t", "p", UserConstants.ROLE_TABLE_TERMINAL, 1, false);
        assertFalse(table.isAdmin());
        assertFalse(table.isKitchen());
        assertFalse(table.isHall());
        assertFalse(table.isCashier());
        assertTrue(table.isTableTerminal());
        assertEquals(1, table.tableId());
    }

    @Test
    void testUserDefaultConstructor() {
        User user = new User();
        assertNull(user.id());
        assertEquals(0, user.role());
    }

    @Test
    void testOrderItemView() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        OrderItemView view = new OrderItemView(1, "Prod", 2, "Tab", now, 10, 100);
        
        assertEquals(1, view.orderItemId());
        assertEquals("Prod", view.productName());
        assertEquals(2, view.quantity());
        assertEquals("Tab", view.tableName());
        assertNotNull(view.orderedAt());
        assertEquals(now.getTime(), view.orderedAt().getTime());
        assertEquals(10, view.status());
        assertEquals(100, view.unitPrice());
        
        // Defensive copy check
        assertNotSame(now, view.orderedAt());
    }

    @Test
    void testTableStatusView() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        TableStatusView view = new TableStatusView(1, "T1", "Label", "Code", 5, 500, now);
        
        assertEquals(1, view.tableId());
        assertEquals("T1", view.tableName());
        assertEquals("Label", view.statusLabel());
        assertEquals("Code", view.statusCode());
        assertEquals(5, view.orderCount());
        assertEquals(500, view.totalAmount());
        assertEquals(now.getTime(), view.lastOrderTime().getTime());
        
        // Defensive copy check
        assertNotSame(now, view.lastOrderTime());
    }

    @Test
    void testDailySales() {
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        DailySales sales = new DailySales(date, 1000, 5);
        assertEquals(date, sales.salesDate());
        assertEquals(1000, sales.amount());
        assertEquals(5, sales.orderCount());
    }

    @Test
    void testProductSales() {
        ProductSales sales = new ProductSales("P1", 10, 1000);
        assertEquals("P1", sales.productName());
        assertEquals(10, sales.totalQuantity());
        assertEquals(1000, sales.totalAmount());
    }

    @Test
    void testTableOrderSummary() {
        TableOrderSummary summary = new TableOrderSummary(1, "T1", java.util.Collections.emptyList(), 3000, 3, 1);
        assertEquals(1, summary.tableId());
        assertEquals("T1", summary.tableName());
        assertEquals(3, summary.orderCount());
        assertEquals(3000, summary.totalAmount());
        assertEquals(1, summary.unservedCount());
        assertNotNull(summary.items());
    }
}
