package controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.Product;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.CategoryService;
import service.ProductService;
import util.AppConstants;
import util.ImageStorageProvider;

@ExtendWith(MockitoExtension.class)
public class ProductAdminServletTest {

    private ProductAdminServlet servlet;

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ImageStorageProvider imageStorageProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private Part filePart;

    @Mock
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    public void setUp() {
        servlet = new ProductAdminServlet(productService, categoryService, imageStorageProvider);
    }

    @Test
    public void testDoGet_ListProducts_Success() throws ServletException, IOException {
        // Arrange
        User adminUser = new User("admin", "pass", 1, null);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(adminUser);

        when(request.getParameter("action")).thenReturn(null);
        when(productService.findAll()).thenReturn(Collections.emptyList());
        when(categoryService.findAll()).thenReturn(Collections.emptyList());
        when(request.getRequestDispatcher(AppConstants.VIEW_ADMIN_PRODUCTS)).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq(AppConstants.ATTR_PRODUCT_LIST), anyList());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_NoPermission() throws ServletException, IOException {
        // Arrange
        User normalUser = new User("u1", "pass", 10, null);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(normalUser);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN);
        verify(requestDispatcher, never()).forward(any(), any());
    }

    @Test
    public void testDoPost_SuccessWithImageUpload() throws ServletException, IOException {
        // Arrange
        User adminUser = new User("admin", "pass", 1, null);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(adminUser);

        int productId = 1;
        Product existingProduct = new Product(productId, 2, "Old Name", 1000, null, null, "v1/old_image", true);

        when(request.getParameter("id")).thenReturn(String.valueOf(productId));
        when(request.getParameter("name")).thenReturn("New Burger");
        when(request.getParameter("categoryId")).thenReturn("2");
        when(request.getParameter("price")).thenReturn("1200");
        when(request.getParameter("description")).thenReturn("Delicious burger");
        when(request.getParameter("allergyInfo")).thenReturn("Wheat");
        when(request.getParameter("isAvailable")).thenReturn("true");

        when(productService.findById(productId)).thenReturn(Optional.of(existingProduct));
        
        when(request.getPart("imageFile")).thenReturn(filePart);
        when(filePart.getSize()).thenReturn(100L);
        when(filePart.getContentType()).thenReturn("image/jpeg");
        when(imageStorageProvider.upload(any(Part.class))).thenReturn("v2/new_image");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(imageStorageProvider).upload(filePart);
        verify(productService).update(any(Product.class), anyString());
        verify(response).sendRedirect(contains("success"));
    }

    @Test
    public void testDoPost_NoPermission() throws ServletException, IOException {
        // Arrange
        User normalUser = new User("u1", "pass", 10, null);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(normalUser);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN);
        verify(productService, never()).update(any(), anyString());
    }

    @Test
    public void testDoGet_EditForm_Success() throws ServletException, IOException {
        User admin = new User("a", "p", 1, null);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("1");
        when(productService.findById(1)).thenReturn(Optional.of(new Product(1, 1, "p", 100, null, null, null, true)));
        when(request.getRequestDispatcher(AppConstants.VIEW_ADMIN_PRODUCT_EDIT)).thenReturn(requestDispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq(AppConstants.ATTR_PRODUCT), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_AddForm() throws ServletException, IOException {
        User admin = new User("a", "p", 1, null);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getRequestDispatcher(AppConstants.VIEW_ADMIN_PRODUCT_EDIT)).thenReturn(requestDispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq(AppConstants.ATTR_PRODUCT), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_Insert_Success() throws ServletException, IOException {
        User admin = new User("admin", "p", 1, null);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("id")).thenReturn("0");
        when(request.getParameter("name")).thenReturn("New");
        when(request.getParameter("categoryId")).thenReturn("1");
        when(request.getParameter("price")).thenReturn("1000");
        when(request.getPart("imageFile")).thenReturn(filePart);
        when(filePart.getSize()).thenReturn(100L);
        when(filePart.getContentType()).thenReturn("image/png");
        when(imageStorageProvider.upload(any())).thenReturn("path");

        servlet.doPost(request, response);

        verify(productService).insert(any(), eq("admin"));
        verify(response).sendRedirect(contains("success"));
    }

    @Test
    public void testDoPost_ValidationError_ImageRequired() throws ServletException, IOException {
        User admin = new User("admin", "p", 1, null);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("id")).thenReturn("0"); // Insert mode
        when(request.getParameter("name")).thenReturn("New");
        when(request.getParameter("categoryId")).thenReturn("1");
        when(request.getParameter("price")).thenReturn("1000");
        when(request.getPart("imageFile")).thenReturn(null); // No image
        when(request.getRequestDispatcher(AppConstants.VIEW_ADMIN_PRODUCT_EDIT)).thenReturn(requestDispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq(AppConstants.ATTR_ERROR), contains("必須"));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_Update_NotFound() throws ServletException, IOException {
        User admin = new User("admin", "p", 1, null);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(admin);
        when(request.getParameter("id")).thenReturn("999");
        when(productService.findById(999)).thenReturn(Optional.empty());

        servlet.doPost(request, response);

        verify(response).sendRedirect(AppConstants.REDIRECT_ADMIN_PRODUCT);
    }
}
