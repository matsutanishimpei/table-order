package controller;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
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
import service.TableService;
import service.UserService;
import util.AppConstants;

@ExtendWith(MockitoExtension.class)
public class UserAdminServletTest {

    private UserAdminServlet servlet;

    @Mock
    private UserService userService;

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
        servlet = new UserAdminServlet(userService, tableService);
    }

    @Test
    public void testDoGet_ListUsers_Success() throws ServletException, IOException {
        // Arrange
        User adminUser = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(adminUser);

        when(request.getParameter("action")).thenReturn(null);
        when(userService.findAll()).thenReturn(new ArrayList<>());
        when(request.getRequestDispatcher(AppConstants.VIEW_ADMIN_USERS)).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq(AppConstants.ATTR_USER_LIST), anyList());
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

    @Test
    public void testDoPost_RegisterUser_Success() throws ServletException, IOException {
        // Arrange
        User adminUser = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(adminUser);

        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("id")).thenReturn("newuser");
        when(request.getParameter("password")).thenReturn("p123");
        when(request.getParameter("role")).thenReturn("10");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(userService).register(any(User.class), anyString());
        verify(response).sendRedirect(AppConstants.REDIRECT_ADMIN_USER);
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
        verify(userService, never()).register(any(), anyString());
    }

    @Test
    public void testDoGet_EditForm_Success() throws ServletException, IOException {
        User admin = new User("admin", "p", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("u1");
        when(userService.findById("u1")).thenReturn(java.util.Optional.of(new User("u1", null, 10, null, false)));
        when(tableService.findAllTableStatus()).thenReturn(new java.util.ArrayList<>());
        when(request.getRequestDispatcher(AppConstants.VIEW_ADMIN_USER_EDIT)).thenReturn(requestDispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("targetUser"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_Update_Success() throws ServletException, IOException {
        User admin = new User("admin", "p", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("u1");
        when(request.getParameter("role")).thenReturn("10");
        when(request.getParameter("password")).thenReturn("pass");
        when(request.getParameter("tableId")).thenReturn(null);

        servlet.doPost(request, response);

        verify(userService).update(any(), any(), eq("admin"));
        verify(response).sendRedirect(AppConstants.REDIRECT_ADMIN_USER);
    }

    @Test
    public void testDoPost_Delete_Success() throws ServletException, IOException {
        User admin = new User("admin", "p", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("u1");
        when(request.getParameter("role")).thenReturn("10");
        when(request.getParameter("password")).thenReturn(null);
        when(request.getParameter("tableId")).thenReturn(null);

        servlet.doPost(request, response);

        verify(userService).delete("u1", "admin");
        verify(response).sendRedirect(AppConstants.REDIRECT_ADMIN_USER);
    }

    @Test
    public void testDoPost_InvalidRoleFormat() throws ServletException, IOException {
        User admin = new User("admin", "p", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        lenient().when(request.getParameter("action")).thenReturn("add");
        lenient().when(request.getParameter("id")).thenReturn("u1");
        lenient().when(request.getParameter("password")).thenReturn(null);
        lenient().when(request.getParameter("role")).thenReturn("abc"); // Invalid

        servlet.doPost(request, response);

        verify(response).sendRedirect(AppConstants.REDIRECT_ADMIN_USER);
        verify(userService, never()).register(any(), anyString());
    }

    @Test
    public void testDoPost_InvalidTableIdFormat() throws ServletException, IOException {
        User admin = new User("admin", "p", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("u1");
        when(request.getParameter("role")).thenReturn("10");
        when(request.getParameter("tableId")).thenReturn("xyz"); // Invalid
        when(request.getParameter("password")).thenReturn(null);

        servlet.doPost(request, response);

        // ValidationUtil.parseIntSafe("xyz", 0) returns 0. 
        // Servlet logic: (parsedTableId <= 0) ? null : parsedTableId;
        // So "xyz" results in tableId = null
        verify(userService).update(argThat(u -> u.tableId() == null), any(), eq("admin"));
        verify(response).sendRedirect(AppConstants.REDIRECT_ADMIN_USER);
    }

    @Test
    public void testDoPost_MissingParameters() throws ServletException, IOException {
        User admin = new User("admin", "p", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        
        lenient().when(request.getParameter("action")).thenReturn("add");
        lenient().when(request.getParameter("id")).thenReturn("u1");
        lenient().when(request.getParameter("password")).thenReturn(null);
        // Missing role returns -1 default, leading to redirect
        lenient().when(request.getParameter("role")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect(AppConstants.REDIRECT_ADMIN_USER);
    }
}
