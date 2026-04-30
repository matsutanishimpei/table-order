package controller;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.OrderService;
import service.TableService;
import util.AppConstants;

@ExtendWith(MockitoExtension.class)
public class CashierServletTest {

    private CashierServlet servlet;

    @Mock
    private OrderService orderService;

    @Mock
    private TableService tableService;

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
        servlet = new CashierServlet(orderService, tableService);
    }

    @Test
    public void testDoGet_DisplaysCashierHome_Success() throws ServletException, IOException {
        // Arrange
        User cashierUser = new User("c1", "pass", 4, null, false); // role 4 = CASHIER
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(cashierUser);

        when(request.getParameter("tableId")).thenReturn(null);
        when(tableService.findUnsettledTables()).thenReturn(Collections.emptyList());
        when(request.getRequestDispatcher(AppConstants.VIEW_CASHIER)).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_AdminCanAccess() throws ServletException, IOException {
        // Arrange
        User adminUser = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(adminUser);

        when(request.getParameter("tableId")).thenReturn(null);
        when(tableService.findUnsettledTables()).thenReturn(Collections.emptyList());
        when(request.getRequestDispatcher(AppConstants.VIEW_CASHIER)).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_CheckoutAction_Success() throws ServletException, IOException {
        // Arrange
        User cashierUser = new User("c1", "pass", 4, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(cashierUser);

        when(request.getParameter("tableId")).thenReturn("5");
        when(request.getParameter("action")).thenReturn("checkout");
        when(orderService.completeCheckout(anyInt(), anyString())).thenReturn(true);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(orderService).completeCheckout(anyInt(), anyString());
        verify(response).sendRedirect(AppConstants.REDIRECT_HOME);
    }

    @Test
    public void testDoPost_AdminCanPerformAction() throws ServletException, IOException {
        // Arrange
        User adminUser = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(adminUser);

        when(request.getParameter("tableId")).thenReturn("5");
        when(request.getParameter("action")).thenReturn("checkout");
        when(orderService.completeCheckout(anyInt(), anyString())).thenReturn(true);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(orderService).completeCheckout(anyInt(), anyString());
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
        verify(orderService, never()).completeCheckout(anyInt(), anyString());
    }
}
