package service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import database.OrderDAO;
import database.TransactionManager;
import database.TransactionExecutor;
import model.CartItem;
import model.OrderItemView;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private Connection connection;

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderDAO);
    }

    @Test
    @DisplayName("正常な注文作成: トランザクション内でDAOが呼ばれること")
    @SuppressWarnings("unchecked")
    void createOrder_Success() throws Exception {
        // Arrange
        int tableId = 1;
        List<CartItem> items = List.of(new CartItem(1, "Product 1", 100, 2));
        
        // TransactionManager.execute を Mock 化
        try (MockedStatic<TransactionManager> mockedStatic = mockStatic(TransactionManager.class)) {
            mockedStatic.when(() -> TransactionManager.execute(any(TransactionExecutor.class)))
                .thenAnswer(invocation -> {
                    TransactionExecutor<?> executor = invocation.getArgument(0);
                    return executor.execute(connection);
                });
            
            when(orderDAO.insertOrder(eq(connection), eq(tableId), anyInt())).thenReturn(100);

            // Act
            boolean result = orderService.createOrder(tableId, items);

            // Assert
            assertTrue(result);
            verify(orderDAO).insertOrder(eq(connection), eq(tableId), anyInt());
            verify(orderDAO).insertOrderItems(eq(connection), eq(100), eq(items), anyInt());
        }
    }

    @Test
    @DisplayName("注文作成失敗: OrderID生成失敗時にfalseを返すこと")
    void createOrder_Failure_OrderIdMinus1() throws Exception {
        // Arrange
        int tableId = 1;
        List<CartItem> items = List.of(new CartItem(1, "Product 1", 100, 2));
        
        try (MockedStatic<TransactionManager> mockedStatic = mockStatic(TransactionManager.class)) {
            mockedStatic.when(() -> TransactionManager.execute(any(TransactionExecutor.class)))
                .thenAnswer(invocation -> {
                    TransactionExecutor<?> executor = invocation.getArgument(0);
                    return executor.execute(connection);
                });
            
            when(orderDAO.insertOrder(eq(connection), eq(tableId), anyInt())).thenReturn(-1);

            // Act
            boolean result = orderService.createOrder(tableId, items);

            // Assert
            assertFalse(result);
        }
    }

    @Test
    @DisplayName("会計完了: トランザクション内で正しく更新されること")
    void completeCheckout_Success() throws Exception {
        // Arrange
        int tableId = 5;
        
        try (MockedStatic<TransactionManager> mockedStatic = mockStatic(TransactionManager.class)) {
            mockedStatic.when(() -> TransactionManager.execute(any(TransactionExecutor.class)))
                .thenAnswer(invocation -> {
                    TransactionExecutor<?> executor = invocation.getArgument(0);
                    return executor.execute(connection);
                });

            // Act
            boolean result = orderService.completeCheckout(tableId);

            // Assert
            assertTrue(result);
            verify(orderDAO).updateOrderItemsStatusForCheckout(eq(connection), eq(tableId), anyInt(), anyInt());
            verify(orderDAO).updateOrderStatusForCheckout(eq(connection), eq(tableId), anyInt(), anyInt());
        }
    }

    @Test
    @DisplayName("調理中商品の検索: DAOの結果が返ること")
    void findActiveOrderItems_ReturnsList() {
        List<OrderItemView> expected = List.of(mock(OrderItemView.class));
        when(orderDAO.findActiveOrderItems()).thenReturn(expected);
        assertEquals(expected, orderService.findActiveOrderItems());
    }

    @Test
    @DisplayName("調理済商品の検索: DAOの結果が返ること")
    void findReadyOrderItems_ReturnsList() {
        List<OrderItemView> expected = List.of(mock(OrderItemView.class));
        when(orderDAO.findReadyOrderItems()).thenReturn(expected);
        assertEquals(expected, orderService.findReadyOrderItems());
    }

    @Test
    @DisplayName("商品ステータス更新: DAOの結果が返ること")
    void updateItemStatus_ReturnsResult() {
        when(orderDAO.updateItemStatus(123, 2)).thenReturn(true);
        assertTrue(orderService.updateItemStatus(123, 2));
    }
}
