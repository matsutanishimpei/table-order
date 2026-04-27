package controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

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
import service.UserService;
import util.CsrfUtil;

@ExtendWith(MockitoExtension.class)
public class LoginServletTest {

    private LoginServlet servlet;

    @Mock
    private UserService userService;

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
        servlet = new LoginServlet(userService);
    }

    @Test
    public void testDoGet_DisplaysLoginPage() throws ServletException, IOException {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(session).setAttribute(eq("csrf_token"), anyString());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_LoginSuccess_Admin() throws ServletException, IOException {
        // Arrange
        String userId = "admin";
        String password = "password";
        String token = "valid-token";
        User adminUser = new User();
        adminUser.setId(userId);
        adminUser.setRole(model.UserConstants.ROLE_ADMIN);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("csrf_token")).thenReturn(token);
        when(request.getParameter("csrf_token")).thenReturn(token);
        when(request.getParameter("id")).thenReturn(userId);
        when(request.getParameter("pw")).thenReturn(password);
        when(userService.login(userId, password)).thenReturn(adminUser);
        when(request.getSession()).thenReturn(session);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).changeSessionId();
        verify(session).setAttribute("user", adminUser);
        verify(response).sendRedirect("Admin/Home");
    }

    @Test
    public void testDoPost_LoginFailure() throws ServletException, IOException {
        // Arrange
        String userId = "invalid";
        String password = "wrong";
        String token = "valid-token";

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("csrf_token")).thenReturn(token);
        when(request.getParameter("csrf_token")).thenReturn(token);
        when(request.getParameter("id")).thenReturn(userId);
        when(request.getParameter("pw")).thenReturn(password);
        when(userService.login(userId, password)).thenReturn(null);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), anyString());
        verify(requestDispatcher).forward(request, response);
    }
}
