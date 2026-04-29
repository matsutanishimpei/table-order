package controller;

import static org.mockito.Mockito.*;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import util.AppConstants;

@ExtendWith(MockitoExtension.class)
public class BaseServletTest {

    // BaseServlet is abstract, so we create a concrete subclass for testing
    private static class TestServlet extends BaseServlet {
        private static final long serialVersionUID = 1L;
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            throw new RuntimeException("Test Exception");
        }
    }

    private TestServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    public void setUp() {
        servlet = new TestServlet();
    }

    @Test
    public void testService_HandlesExceptionAndForwardsToErrorPage() throws ServletException, IOException {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(response.isCommitted()).thenReturn(false);
        when(request.getRequestDispatcher(AppConstants.VIEW_ERROR)).thenReturn(requestDispatcher);

        // Act
        servlet.service(request, response);

        // Assert
        verify(request).setAttribute(eq(AppConstants.ATTR_ERROR_MESSAGE), anyString());
        verify(request).setAttribute(eq(AppConstants.ATTR_EXCEPTION), any(RuntimeException.class));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testService_WhenResponseCommitted_ThrowsServletException() throws ServletException, IOException {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(response.isCommitted()).thenReturn(true);

        // Act & Assert
        try {
            servlet.service(request, response);
        } catch (ServletException e) {
            // Success
            return;
        }
        throw new AssertionError("Expected ServletException was not thrown");
    }
}
