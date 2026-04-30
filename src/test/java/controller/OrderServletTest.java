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
        // User record: id, password, role, tableId, isDeleted
        User user = new User("table1", "pass", 10, 1, false);
        List<CartItem> cart = new ArrayList<>();
        cart.add(new CartItem(1, "Product 1", 100, 2));

        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(session.getAttribute(AppConstants.ATTR_CART)).thenReturn(cart);
        when(orderService.createOrder(eq(1), anyList(), anyString())).thenReturn(true);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(orderService).createOrder(eq(1), anyList(), eq("table1"));
        verify(response).sendRedirect(contains("msg=success"));
        assertTrue(cart.isEmpty());
    }

    @Test
    @DisplayName("注文確定失敗: ユーザー未ログイン時はメニューへリダイレクトされること")
    void doPost_NoUser_Redirects() throws ServletException, IOException {
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect(AppConstants.REDIRECT_MENU);
        verify(orderService, never()).createOrder(anyInt(), anyList(), anyString());
    }

    @Test
    @DisplayName("注文確定失敗: テーブルID未設定時はエラーメッセージを表示してフォワードされること")
    void doPost_NoTableId() throws ServletException, IOException {
        User user = new User("admin", "pass", 1, null, false); // tableId = null
        List<CartItem> cart = new ArrayList<>();
        cart.add(new CartItem(1, "Product 1", 100, 2));

        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(session.getAttribute(AppConstants.ATTR_CART)).thenReturn(cart);
        jakarta.servlet.RequestDispatcher rd = mock(jakarta.servlet.RequestDispatcher.class);
        when(request.getRequestDispatcher(AppConstants.VIEW_MENU)).thenReturn(rd);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq(AppConstants.ATTR_ERROR), anyString());
        verify(rd).forward(request, response);
    }

    @Test
    @DisplayName("注文確定失敗: サービス層でエラーが発生した場合はエラーメッセージ付きでリダイレクトされること")
    void doPost_ServiceFailure() throws ServletException, IOException {
        User user = new User("table1", "pass", 10, 1, false);
        List<CartItem> cart = new ArrayList<>();
        cart.add(new CartItem(1, "Product 1", 100, 2));

        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(session.getAttribute(AppConstants.ATTR_CART)).thenReturn(cart);
        when(orderService.createOrder(anyInt(), anyList(), anyString())).thenReturn(false);

        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("msg=error"));
    }

    @Test
    @DisplayName("注文確定失敗: カートが空の場合はメニューへリダイレクトされること")
    void doPost_EmptyCart() throws ServletException, IOException {
        User user = new User("table1", "pass", 10, 1, false);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(session.getAttribute(AppConstants.ATTR_CART)).thenReturn(new ArrayList<>());

        servlet.doPost(request, response);

        verify(response).sendRedirect(AppConstants.REDIRECT_MENU);
    }

    @Test
    @DisplayName("注文確定失敗: カートがセッションに存在しない場合はメニューへリダイレクトされること")
    void doPost_NullCart() throws ServletException, IOException {
        User user = new User("table1", "pass", 10, 1, false);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(session.getAttribute(AppConstants.ATTR_CART)).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect(AppConstants.REDIRECT_MENU);
    }
}
