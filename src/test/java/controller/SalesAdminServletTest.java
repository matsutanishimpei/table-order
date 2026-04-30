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
import service.SalesService;
import util.AppConstants;

@ExtendWith(MockitoExtension.class)
public class SalesAdminServletTest {

    private SalesAdminServlet servlet;

    @Mock
    private SalesService salesService;

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
        servlet = new SalesAdminServlet(salesService);
    }

    @Test
    public void testDoGet_DisplaysSalesAdmin_Success() throws ServletException, IOException {
        // Arrange
        User adminUser = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(adminUser);

        when(salesService.getTotalSales()).thenReturn(10000);
        when(salesService.findDailySales()).thenReturn(Collections.emptyList());
        when(salesService.findProductSalesRanking()).thenReturn(Collections.emptyList());
        when(request.getRequestDispatcher(AppConstants.VIEW_ADMIN_SALES)).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(AppConstants.ATTR_TOTAL_SALES, 10000);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_NoPermission() throws ServletException, IOException {
        // Arrange
        User normalUser = new User("u1", "pass", 10, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(normalUser);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN);
        verify(requestDispatcher, never()).forward(any(), any());
    }
}
