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
import service.TableService;
import util.AppConstants;

@ExtendWith(MockitoExtension.class)
public class OrderMonitorServletTest {

    private OrderMonitorServlet servlet;

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
        servlet = new OrderMonitorServlet(tableService);
    }

    @Test
    public void testDoGet_DisplaysMonitor_Success() throws ServletException, IOException {
        // Arrange
        User adminUser = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(adminUser);

        when(tableService.findAllTableStatus()).thenReturn(Collections.emptyList());
        when(request.getRequestDispatcher(AppConstants.VIEW_ADMIN_ORDER_MONITOR)).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq(AppConstants.ATTR_TABLE_LIST), anyList());
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
