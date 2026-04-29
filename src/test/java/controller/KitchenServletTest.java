package controller;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.OrderItemView;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.OrderService;
import util.AppConstants;

@ExtendWith(MockitoExtension.class)
public class KitchenServletTest {

    private KitchenServlet servlet;

    @Mock
    private OrderService orderService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    public void setUp() {
        servlet = new KitchenServlet(orderService);
    }

    @Test
    public void testDoGet_DisplaysKitchen() throws ServletException, IOException {
        // Arrange
        List<OrderItemView> orders = Collections.emptyList();
        when(orderService.findActiveOrderItems()).thenReturn(orders);
        when(request.getRequestDispatcher(AppConstants.VIEW_KITCHEN)).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(AppConstants.ATTR_ACTIVE_ITEMS, orders);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_CompleteAction_Success() throws ServletException, IOException {
        // Arrange
        User kitchenUser = new User("k1", "pass", 2, null); // role 2 = KITCHEN
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(kitchenUser);
        
        when(request.getParameter("itemId")).thenReturn("123");
        when(request.getParameter("action")).thenReturn("complete");
        when(orderService.updateItemStatus(123, model.OrderConstants.STATUS_COOKING_DONE, "k1")).thenReturn(true);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(orderService).updateItemStatus(anyInt(), anyInt(), anyString());
        verify(response).sendRedirect(AppConstants.REDIRECT_HOME);
    }

    @Test
    public void testDoPost_AdminCanPerformAction() throws ServletException, IOException {
        // Arrange
        User adminUser = new User("admin", "pass", 1, null); // role 1 = ADMIN
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(adminUser);

        when(request.getParameter("itemId")).thenReturn("123");
        when(request.getParameter("action")).thenReturn("complete");
        when(orderService.updateItemStatus(123, model.OrderConstants.STATUS_COOKING_DONE, "admin")).thenReturn(true);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(orderService).updateItemStatus(anyInt(), anyInt(), anyString());
        verify(response).sendRedirect(AppConstants.REDIRECT_HOME);
    }

    @Test
    public void testDoPost_NoPermission() throws ServletException, IOException {
        // Arrange
        User normalUser = new User("u1", "pass", 10, null); // role 10 = TABLE_TERMINAL
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(normalUser);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN);
        verify(orderService, never()).updateItemStatus(anyInt(), anyInt(), anyString());
    }

    @Test
    public void testDoPost_InvalidId() throws ServletException, IOException {
        // Arrange
        User kitchenUser = new User("k1", "pass", 2, null);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(kitchenUser);
        when(request.getParameter("itemId")).thenReturn("invalid");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(orderService, never()).updateItemStatus(anyInt(), anyInt(), anyString());
        verify(response).sendRedirect(AppConstants.REDIRECT_HOME);
    }
}
