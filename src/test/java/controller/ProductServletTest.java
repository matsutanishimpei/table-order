package controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductServletTest {

    private ProductServlet servlet;

    @Mock
    private ProductService productService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    public void setUp() {
        servlet = new ProductServlet(productService);
    }

    @Test
    public void testDoGet_Success() throws ServletException, IOException {
        // Arrange
        int productId = 101;
        Product p = new Product();
        p.setId(productId);
        p.setName("Test Product");

        when(request.getParameter("id")).thenReturn(String.valueOf(productId));
        when(request.getParameter("categoryId")).thenReturn("2");
        when(productService.findById(productId)).thenReturn(Optional.of(p));
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute("product", p);
        verify(request).setAttribute("selectedCategoryId", 2);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_ProductNotFound() throws ServletException, IOException {
        // Arrange
        int productId = 999;
        when(request.getParameter("id")).thenReturn(String.valueOf(productId));
        when(productService.findById(productId)).thenReturn(Optional.empty());

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).sendRedirect("Menu");
    }
}
