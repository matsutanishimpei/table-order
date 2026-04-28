package controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CartItem;
import model.User;
import service.OrderService;
import util.AppConstants;

@ExtendWith(MockitoExtension.class)
class OrderServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private OrderService orderService;

    private OrderServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new OrderServlet(orderService);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    @DisplayName("注文確定成功: カートがクリアされメニュー画面へリダイレクトされること")
    void doPost_Success() throws ServletException, IOException {
        // Arrange
        // User record: id, password, role, tableId
        User user = new User("table1", "pass", 10, 1);
        List<CartItem> cart = new ArrayList<>();
        cart.add(new CartItem(1, "Product 1", 100, 2));

        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(session.getAttribute(AppConstants.ATTR_CART)).thenReturn(cart);
        when(orderService.createOrder(eq(1), anyList())).thenReturn(true);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(orderService).createOrder(eq(1), anyList());
        verify(response).sendRedirect(contains("msg=success"));
        assertTrue(cart.isEmpty());
    }

    @Test
    @DisplayName("注文確定失敗: ユーザー未ログイン時はメニューへリダイレクトされること")
    void doPost_NoUser_Redirects() throws ServletException, IOException {
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect(AppConstants.REDIRECT_MENU);
        verify(orderService, never()).createOrder(anyInt(), anyList());
    }
}
