package controller;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.TableOrderSummary;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.TableService;
import util.AppConstants;

@ExtendWith(MockitoExtension.class)
public class OrderHistoryServletTest {

    private OrderHistoryServlet servlet;

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
        servlet = new OrderHistoryServlet(tableService);
    }

    @Test
    public void testDoGet_DisplaysHistory() throws ServletException, IOException {
        // Arrange
        User user = new User("test", null, 0, 5); // tableId = 5
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        
        TableOrderSummary summary = mock(TableOrderSummary.class);
        when(tableService.getTableOrderSummary(5)).thenReturn(Optional.of(summary));
        when(request.getRequestDispatcher(AppConstants.VIEW_ORDER_HISTORY)).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute("summary", summary);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_WhenNotLoggedIn_RedirectsToLogin() throws ServletException, IOException {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(null);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).sendRedirect(AppConstants.REDIRECT_LOGIN);
    }
}
