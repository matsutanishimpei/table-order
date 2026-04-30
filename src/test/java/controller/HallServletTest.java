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
public class HallServletTest {

    private HallServlet servlet;

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
        servlet = new HallServlet(orderService);
    }

    @Test
    public void testDoGet_DisplaysHall() throws ServletException, IOException {
        // Arrange
        List<OrderItemView> orders = Collections.emptyList();
        when(orderService.findReadyOrderItems()).thenReturn(orders);
        when(request.getRequestDispatcher(AppConstants.VIEW_HALL)).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(AppConstants.ATTR_READY_ITEMS, orders);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_ServeAction_Success() throws ServletException, IOException {
        // Arrange
        User hallUser = new User("h1", "pass", 3, null, false); // role 3 = HALL
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(hallUser);

        when(request.getParameter("itemId")).thenReturn("456");
        when(request.getParameter("action")).thenReturn("serve");
        when(orderService.updateItemStatus(anyInt(), anyInt(), anyString())).thenReturn(true);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(orderService).updateItemStatus(anyInt(), anyInt(), anyString());
        verify(response).sendRedirect(AppConstants.REDIRECT_HOME);
    }

    @Test
    public void testDoPost_AdminCanPerformAction() throws ServletException, IOException {
        // Arrange
        User adminUser = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(adminUser);

        when(request.getParameter("itemId")).thenReturn("456");
        when(request.getParameter("action")).thenReturn("serve");
        when(orderService.updateItemStatus(anyInt(), anyInt(), anyString())).thenReturn(true);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(orderService).updateItemStatus(anyInt(), anyInt(), anyString());
        verify(response).sendRedirect(AppConstants.REDIRECT_HOME);
    }

    @Test
    public void testDoPost_NoPermission() throws ServletException, IOException {
        // Arrange
        User normalUser = new User("u1", "pass", 10, null, false);
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
        User hallUser = new User("h1", "pass", 3, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(hallUser);
        when(request.getParameter("itemId")).thenReturn("invalid");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(orderService, never()).updateItemStatus(anyInt(), anyInt(), anyString());
        verify(response).sendRedirect(AppConstants.REDIRECT_HOME);
    }
}
