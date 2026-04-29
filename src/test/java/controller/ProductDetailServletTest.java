package controller;

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
public class ProductDetailServletTest {

    private ProductDetailServlet servlet;

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
        servlet = new ProductDetailServlet(productService);
    }

    @Test
    public void testDoGet_DisplaysProductDetail() throws ServletException, IOException {
        // Arrange
        Product product = mock(Product.class);
        when(request.getParameter("productId")).thenReturn("1");
        when(productService.findById(1)).thenReturn(Optional.of(product));
        when(request.getRequestDispatcher("/WEB-INF/view/product_detail.jsp")).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute("product", product);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_InvalidId_RedirectsToMenu() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("productId")).thenReturn("abc");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).sendRedirect("Menu");
    }

    @Test
    public void testDoGet_ProductNotFound_RedirectsToMenu() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("productId")).thenReturn("999");
        when(productService.findById(999)).thenReturn(Optional.empty());

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).sendRedirect("Menu");
    }
}
