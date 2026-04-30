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
import service.CategoryService;
import util.AppConstants;

@ExtendWith(MockitoExtension.class)
public class CategoryAdminServletTest {

    private CategoryAdminServlet servlet;

    @Mock
    private CategoryService categoryService;

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
        servlet = new CategoryAdminServlet(categoryService);
    }

    @Test
    public void testDoGet_ListCategories_Success() throws ServletException, IOException {
        // Arrange
        User adminUser = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(adminUser);

        when(request.getParameter("action")).thenReturn(null);
        when(categoryService.findAll()).thenReturn(Collections.emptyList());
        when(request.getRequestDispatcher(AppConstants.VIEW_ADMIN_CATEGORIES)).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq(AppConstants.ATTR_CATEGORY_LIST), anyList());
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
    public void testDoPost_InsertCategory_Success() throws ServletException, IOException {
        // Arrange
        User adminUser = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(adminUser);

        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("name")).thenReturn("New Category");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(categoryService).insert(eq("New Category"), anyString());
        verify(response).sendRedirect(contains("success"));
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
        verify(categoryService, never()).insert(anyString(), anyString());
    }

    @Test
    public void testDoGet_EditForm_Success() throws ServletException, IOException {
        User admin = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("1");
        when(categoryService.findById(1)).thenReturn(java.util.Optional.of(new model.Category(1, "cat", false)));
        when(request.getRequestDispatcher(AppConstants.VIEW_ADMIN_CATEGORY_EDIT)).thenReturn(requestDispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq(AppConstants.ATTR_CATEGORY), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_Update_Success() throws ServletException, IOException {
        User admin = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("name")).thenReturn("Updated");

        servlet.doPost(request, response);

        verify(categoryService).update(any(), eq("admin"));
        verify(response).sendRedirect(contains("success"));
    }

    @Test
    public void testDoPost_Update_InvalidId() throws ServletException, IOException {
        User admin = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("0");
        when(request.getParameter("name")).thenReturn("any");

        servlet.doPost(request, response);

        verify(response).sendRedirect(AppConstants.REDIRECT_ADMIN_CATEGORY);
    }

    @Test
    public void testDoPost_Insert_BusinessException() throws ServletException, IOException {
        User admin = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("name")).thenReturn("");
        doThrow(new exception.BusinessException("Error")).when(categoryService).insert(anyString(), anyString());
        when(request.getRequestDispatcher(AppConstants.VIEW_ADMIN_CATEGORIES)).thenReturn(requestDispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq(AppConstants.ATTR_ERROR), eq("Error"));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_Update_BusinessException() throws ServletException, IOException {
        User admin = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("name")).thenReturn("Invalid");
        doThrow(new exception.BusinessException("Error")).when(categoryService).update(any(), anyString());
        when(request.getRequestDispatcher(AppConstants.VIEW_ADMIN_CATEGORY_EDIT)).thenReturn(requestDispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq(AppConstants.ATTR_ERROR), eq("Error"));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_Delete_Success() throws ServletException, IOException {
        User admin = new User("admin", "pass", 1, null, false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("3");
        when(request.getParameter("name")).thenReturn(null);

        servlet.doPost(request, response);

        verify(categoryService).delete(3, "admin");
        verify(response).sendRedirect(contains("success"));
    }
}
