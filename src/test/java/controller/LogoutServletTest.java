package controller;

import static org.mockito.Mockito.*;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import util.AppConstants;

@ExtendWith(MockitoExtension.class)
public class LogoutServletTest {

    private LogoutServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        servlet = new LogoutServlet();
    }

    @Test
    public void testDoGet_InvalidatesSessionAndRedirects() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/test");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(session).invalidate();
        verify(response).sendRedirect("/test/" + AppConstants.REDIRECT_LOGIN);
    }

    @Test
    public void testDoGet_WhenNoSession_RedirectsOnly() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/test");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).sendRedirect("/test/" + AppConstants.REDIRECT_LOGIN);
    }

    @Test
    public void testDoPost_CallsDoGet() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/test");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).sendRedirect("/test/" + AppConstants.REDIRECT_LOGIN);
    }
}
