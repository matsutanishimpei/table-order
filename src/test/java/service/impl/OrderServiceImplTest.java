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
import database.ProductDAO;
import database.TransactionManager;
import database.TransactionExecutor;
import model.CartItem;
import model.OrderItemView;
import model.Product;
import model.OrderConstants;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private ProductDAO productDAO;

    @Mock
    private Connection connection;

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderDAO, productDAO);
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

            // 商品は利用可能として設定
            Product p = mock(Product.class);
            when(p.isAvailable()).thenReturn(true);
            when(productDAO.findById(anyInt())).thenReturn(java.util.Optional.of(p));
            // 既存注文なし
            when(orderDAO.findActiveOrderIdByTable(eq(connection), eq(tableId))).thenReturn(-1);
            when(orderDAO.insertOrder(eq(connection), eq(tableId), anyInt(), anyString())).thenReturn(100);

            // Act
            boolean result = orderService.createOrder(tableId, items, "test-user");

            // Assert
            assertTrue(result);
            verify(orderDAO).insertOrder(eq(connection), eq(tableId), anyInt(), eq("test-user"));
            verify(orderDAO).insertOrderItems(eq(connection), eq(100), eq(items), anyInt(), eq("test-user"));
        }
    }

    @Test
    @DisplayName("注文作成失敗: OrderID生成失敗時にfalseを返すこと")
    @SuppressWarnings("unchecked")
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

            // 商品は利用可能
            Product p = mock(Product.class);
            when(p.isAvailable()).thenReturn(true);
            when(productDAO.findById(anyInt())).thenReturn(java.util.Optional.of(p));

            // 既存注文なし
            when(orderDAO.findActiveOrderIdByTable(eq(connection), eq(tableId))).thenReturn(-1);
            when(orderDAO.insertOrder(eq(connection), eq(tableId), anyInt(), anyString())).thenReturn(-1);

            // Act
            boolean result = orderService.createOrder(tableId, items, "test-user");

            // Assert
            assertFalse(result);
        }
    }

    @Test
    @DisplayName("会計完了: トランザクション内で正しく更新されること")
    @SuppressWarnings("unchecked")
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
            boolean result = orderService.completeCheckout(tableId, "test-user");

            // Assert
            assertTrue(result);
            verify(orderDAO).updateOrderItemsStatusForCheckout(eq(connection), eq(tableId), anyInt(), anyInt(), eq("test-user"));
            verify(orderDAO).updateOrderStatusForCheckout(eq(connection), eq(tableId), anyInt(), anyInt(), eq("test-user"));
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
        when(orderDAO.findItemStatusById(123)).thenReturn(OrderConstants.STATUS_ORDERED);
        when(orderDAO.updateItemStatus(123, OrderConstants.STATUS_COOKING_DONE, "test-user")).thenReturn(true);
        assertTrue(orderService.updateItemStatus(123, OrderConstants.STATUS_COOKING_DONE, "test-user"));
    }

    @Test
    @DisplayName("商品ステータス更新失敗: 過去のステータスへの逆行を防ぐこと")
    void updateItemStatus_Failure_BackwardTransition() {
        // 現在が「配膳済み(30)」
        when(orderDAO.findItemStatusById(123)).thenReturn(OrderConstants.STATUS_SERVED);

        // 「調理待ち(10)」に戻そうとする
        boolean result = orderService.updateItemStatus(123, OrderConstants.STATUS_ORDERED, "test-user");

        assertFalse(result, "ステータスの逆行は拒否されるべき");
        verify(orderDAO, never()).updateItemStatus(anyInt(), anyInt(), anyString());
    }

    @Test
    @DisplayName("部分失敗時のロールバック: order_items登録失敗時に例外がスローされ、falseが返ること")
    @SuppressWarnings("unchecked")
    void createOrder_PartialFailure_Rollback() throws Exception {
        // Arrange
        int tableId = 1;
        List<CartItem> items = List.of(new CartItem(1, "Product 1", 100, 2));

        try (MockedStatic<TransactionManager> mockedStatic = mockStatic(TransactionManager.class)) {
            mockedStatic.when(() -> TransactionManager.execute(any(TransactionExecutor.class)))
                    .thenAnswer(invocation -> {
                        TransactionExecutor<?> executor = invocation.getArgument(0);
                        return executor.execute(connection);
                    });

            // 商品は利用可能
            Product p = mock(Product.class);
            when(p.isAvailable()).thenReturn(true);
            when(productDAO.findById(anyInt())).thenReturn(java.util.Optional.of(p));

            // 既存注文なし
            when(orderDAO.findActiveOrderIdByTable(eq(connection), eq(tableId))).thenReturn(-1);
            // ① 最初の注文登録は成功
            when(orderDAO.insertOrder(eq(connection), eq(tableId), anyInt(), anyString())).thenReturn(100);
            // ② 続く明細登録で例外発生（DBエラー等を想定）
            doThrow(new RuntimeException("DB Error during items insertion"))
                    .when(orderDAO).insertOrderItems(eq(connection), eq(100), eq(items), anyInt(), anyString());

            // Act
            boolean result = orderService.createOrder(tableId, items, "test-user");

            // Assert
            assertFalse(result, "例外発生時は false が返るべき");
            verify(orderDAO).insertOrder(eq(connection), eq(tableId), anyInt(), eq("test-user"));
            verify(orderDAO).insertOrderItems(eq(connection), eq(100), eq(items), anyInt(), eq("test-user"));
            // TransactionManager 内でロールバックが行われるはず（モック化しているため挙動のみ確認）
        }
    }

    @Test
    @DisplayName("会計失敗: 未提供の商品が残っている場合にfalseを返すこと")
    @SuppressWarnings("unchecked")
    void completeCheckout_Failure_UnservedItemsExist() throws Exception {
        // Arrange
        int tableId = 5;

        try (MockedStatic<TransactionManager> mockedStatic = mockStatic(TransactionManager.class)) {
            mockedStatic.when(() -> TransactionManager.execute(any(TransactionExecutor.class)))
                    .thenAnswer(invocation -> {
                        TransactionExecutor<?> executor = invocation.getArgument(0);
                        return executor.execute(connection);
                    });

            // 未提供商品が 2 件ある状態をシミュレート
            when(orderDAO.countUnservedItemsByTable(eq(connection), eq(tableId))).thenReturn(2);

            // Act
            boolean result = orderService.completeCheckout(tableId, "test-user");

            // Assert
            assertFalse(result, "未提供商品がある場合は会計失敗（false）になるべき");
            // update メソッドが呼ばれていないことを確認
            verify(orderDAO, never()).updateOrderItemsStatusForCheckout(any(), anyInt(), anyInt(), anyInt(), anyString());
        }
    }

    @Test
    @DisplayName("注文失敗: 非公開商品が含まれる場合にfalseを返すこと")
    @SuppressWarnings("unchecked")
    void createOrder_Failure_ProductUnavailable() throws Exception {
        // Arrange
        int tableId = 1;
        List<CartItem> items = List.of(new CartItem(1, "Product 1", 100, 2));

        try (MockedStatic<TransactionManager> mockedStatic = mockStatic(TransactionManager.class)) {
            mockedStatic.when(() -> TransactionManager.execute(any(TransactionExecutor.class)))
                    .thenAnswer(invocation -> {
                        TransactionExecutor<?> executor = invocation.getArgument(0);
                        return executor.execute(connection);
                    });

            // 商品が非公開（isAvailable = false）の状態をシミュレート
            Product unavailableProduct = mock(Product.class);
            when(unavailableProduct.isAvailable()).thenReturn(false);
            when(productDAO.findById(anyInt())).thenReturn(java.util.Optional.of(unavailableProduct));

            // Act
            boolean result = orderService.createOrder(tableId, items, "test-user");

            // Assert
            assertFalse(result, "非公開商品がある場合は注文失敗（false）になるべき");
            // DAO の insert メソッドが呼ばれていないことを確認
            verify(orderDAO, never()).insertOrder(any(), anyInt(), anyInt(), anyString());
        }
    }

    @Test
    @DisplayName("注文作成: 既存のアクティブな注文がある場合は再利用すること")
    @SuppressWarnings("unchecked")
    void createOrder_ReuseExistingOrder() throws Exception {
        // Arrange
        int tableId = 1;
        int existingOrderId = 500;
        List<CartItem> items = List.of(new CartItem(1, "Product 1", 100, 2));

        try (MockedStatic<TransactionManager> mockedStatic = mockStatic(TransactionManager.class)) {
            mockedStatic.when(() -> TransactionManager.execute(any(TransactionExecutor.class)))
                    .thenAnswer(invocation -> {
                        TransactionExecutor<?> executor = invocation.getArgument(0);
                        return executor.execute(connection);
                    });

            // 商品は利用可能
            Product p = mock(Product.class);
            when(p.isAvailable()).thenReturn(true);
            when(productDAO.findById(anyInt())).thenReturn(java.util.Optional.of(p));

            // 既存注文(500)がある状態をシミュレート
            when(orderDAO.findActiveOrderIdByTable(eq(connection), eq(tableId))).thenReturn(existingOrderId);

            // Act
            boolean result = orderService.createOrder(tableId, items, "test-user");

            // Assert
            assertTrue(result);
            // insertOrder が呼ばれず、既存の existingOrderId に対して明細が登録されること
            verify(orderDAO, never()).insertOrder(any(), anyInt(), anyInt(), anyString());
            verify(orderDAO).insertOrderItems(eq(connection), eq(existingOrderId), any(), anyInt(), eq("test-user"));
        }
    }

    @Test
    @DisplayName("注文失敗: 商品が存在しない場合にfalseを返すこと")
    @SuppressWarnings("unchecked")
    void createOrder_Failure_ProductNotFound() throws Exception {
        // Arrange
        int tableId = 1;
        List<CartItem> items = List.of(new CartItem(999, "Non-existent", 100, 1));

        try (MockedStatic<TransactionManager> mockedStatic = mockStatic(TransactionManager.class)) {
            mockedStatic.when(() -> TransactionManager.execute(any(TransactionExecutor.class)))
                    .thenAnswer(invocation -> {
                        TransactionExecutor<?> executor = invocation.getArgument(0);
                        return executor.execute(connection);
                    });

            // 商品が見つからない（Optional.empty）状態をシミュレート
            when(productDAO.findById(999)).thenReturn(java.util.Optional.empty());

            // Act
            boolean result = orderService.createOrder(tableId, items, "test-user");

            // Assert
            assertFalse(result);
            verify(orderDAO, never()).insertOrder(any(), anyInt(), anyInt(), anyString());
        }
    }

    @Test
    @DisplayName("会計失敗: 例外発生時にfalseを返すこと")
    @SuppressWarnings("unchecked")
    void completeCheckout_Failure_Exception() throws Exception {
        // Arrange
        int tableId = 5;

        try (MockedStatic<TransactionManager> mockedStatic = mockStatic(TransactionManager.class)) {
            // TransactionManager.execute 自体が例外を投げるケースを想定
            mockedStatic.when(() -> TransactionManager.execute(any(TransactionExecutor.class)))
                    .thenThrow(new RuntimeException("Unexpected DB Error"));

            // Act
            boolean result = orderService.completeCheckout(tableId, "test-user");

            // Assert
            assertFalse(result, "例外発生時は false が返るべき");
        }
    }

    @Test
    @DisplayName("商品ステータス更新失敗: 指定された明細が存在しない場合にfalseを返すこと")
    void updateItemStatus_Failure_ItemNotFound() {
        // DAO が -1 を返す（存在しない）
        when(orderDAO.findItemStatusById(999)).thenReturn(-1);

        // Act
        boolean result = orderService.updateItemStatus(999, OrderConstants.STATUS_SERVED, "test-user");

        // Assert
        assertFalse(result);
        verify(orderDAO, never()).updateItemStatus(anyInt(), anyInt(), anyString());
    }
}
