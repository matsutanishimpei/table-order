package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ModelConstantsTest {

    @Test
    void testOrderConstants() {
        assertEquals(10, OrderConstants.STATUS_ORDERED);
        assertEquals(20, OrderConstants.STATUS_COOKING_DONE);
        assertEquals(30, OrderConstants.STATUS_SERVED);
        assertEquals(40, OrderConstants.STATUS_PAID);
    }

    @Test
    void testUserConstants() {
        assertEquals(1, UserConstants.ROLE_ADMIN);
        assertEquals(2, UserConstants.ROLE_KITCHEN);
        assertEquals(3, UserConstants.ROLE_HALL);
        assertEquals(4, UserConstants.ROLE_CASHIER);
        assertEquals(10, UserConstants.ROLE_TABLE_TERMINAL);
    }
}
