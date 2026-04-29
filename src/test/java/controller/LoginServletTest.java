package controller;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Optional;

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
        User adminUser = new User(userId, null, model.UserConstants.ROLE_ADMIN, null);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("csrf_token")).thenReturn(token);
        when(request.getParameter("csrf_token")).thenReturn(token);
        when(request.getParameter("id")).thenReturn(userId);
        when(request.getParameter("pw")).thenReturn(password);
        when(userService.login(userId, password)).thenReturn(Optional.of(adminUser));
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
        when(userService.login(userId, password)).thenReturn(Optional.empty());
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq(util.AppConstants.ATTR_ERROR), anyString());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_LoginFailure_EmptyFields() throws ServletException, IOException {
        // Arrange
        String token = "valid-token";

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("csrf_token")).thenReturn(token);
        when(request.getParameter("csrf_token")).thenReturn(token);
        when(request.getParameter("id")).thenReturn(""); // 空
        when(request.getParameter("pw")).thenReturn("password");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // Act
        servlet.doPost(request, response);

        // Assert
        // userService.login() は呼ばれず、バリデーションエラーメッセージがセットされることを確認
        verify(userService, never()).login(anyString(), anyString());
        verify(request).setAttribute(eq(util.AppConstants.ATTR_ERROR), contains("必須入力"));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_CsrfMismatch() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("csrf_token")).thenReturn("token1");
        when(request.getParameter("csrf_token")).thenReturn("token2"); // 不一致

        servlet.doPost(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), anyString());
    }

    @Test
    public void testDoPost_LoginSuccess_Kitchen() throws ServletException, IOException {
        User kitchenUser = new User("k1", null, model.UserConstants.ROLE_KITCHEN, null);
        String token = "t";
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("csrf_token")).thenReturn(token);
        when(request.getParameter("csrf_token")).thenReturn(token);
        when(request.getParameter("id")).thenReturn("k1");
        when(request.getParameter("pw")).thenReturn("p");
        when(userService.login("k1", "p")).thenReturn(Optional.of(kitchenUser));
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("Kitchen/Home"));
    }

    @Test
    public void testDoPost_LoginSuccess_Hall() throws ServletException, IOException {
        User hallUser = new User("h1", null, model.UserConstants.ROLE_HALL, null);
        String token = "t";
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("csrf_token")).thenReturn(token);
        when(request.getParameter("csrf_token")).thenReturn(token);
        when(request.getParameter("id")).thenReturn("h1");
        when(request.getParameter("pw")).thenReturn("p");
        when(userService.login("h1", "p")).thenReturn(Optional.of(hallUser));
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("Hall/Home"));
    }

    @Test
    public void testDoPost_LoginSuccess_Cashier() throws ServletException, IOException {
        User cashierUser = new User("c1", null, model.UserConstants.ROLE_CASHIER, null);
        String token = "t";
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("csrf_token")).thenReturn(token);
        when(request.getParameter("csrf_token")).thenReturn(token);
        when(request.getParameter("id")).thenReturn("c1");
        when(request.getParameter("pw")).thenReturn("p");
        when(userService.login("c1", "p")).thenReturn(Optional.of(cashierUser));
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("Cashier/Home"));
    }

    @Test
    public void testDoPost_LoginSuccess_Table() throws ServletException, IOException {
        User tableUser = new User("t1", null, model.UserConstants.ROLE_TABLE_TERMINAL, 1);
        String token = "t";
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("csrf_token")).thenReturn(token);
        when(request.getParameter("csrf_token")).thenReturn(token);
        when(request.getParameter("id")).thenReturn("t1");
        when(request.getParameter("pw")).thenReturn("p");
        when(userService.login("t1", "p")).thenReturn(Optional.of(tableUser));
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("Menu"));
    }
}
