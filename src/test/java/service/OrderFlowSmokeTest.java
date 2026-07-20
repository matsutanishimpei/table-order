package service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import database.BaseIntegrationTest;
import java.util.List;
import model.CartItem;
import model.OrderConstants;
import model.OrderItemView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.impl.OrderServiceImpl;

/**
 * 実際のService、DAO、MySQLを使い、注文の代表的なライフサイクルを1本通します。
 * ブラウザ操作は対象外とし、業務ロジックとDBの接続点を軽量に検証します。
 */
class OrderFlowSmokeTest extends BaseIntegrationTest {

    private final OrderService orderService = new OrderServiceImpl();

    @Test
    @DisplayName("注文から調理、配膳、会計まで完了できること")
    void completeRepresentativeOrderFlow() {
        List<CartItem> cart = List.of(new CartItem(1, "表示時の商品名", 1, 2));

        assertTrue(orderService.createOrder(1, cart, "t1"));

        OrderItemView orderedItem = orderService.findActiveOrderItems().get(0);
        int itemId = orderedItem.orderItemId();
        assertTrue(orderService.updateItemStatus(
                itemId, OrderConstants.STATUS_COOKING_DONE, "kitchen"));
        assertTrue(orderService.updateItemStatus(
                itemId, OrderConstants.STATUS_SERVED, "hall"));
        assertTrue(orderService.completeCheckout(1, "cashier"));
    }
}
