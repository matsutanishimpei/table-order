package controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.CategoryService;
import service.ProductService;

@ExtendWith(MockitoExtension.class)
public class MenuServletTest {

    private MenuServlet servlet;

    @Mock
    private CategoryService categoryService;

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
        servlet = new MenuServlet(categoryService, productService);
    }

    @Test
    public void testDoGet_DisplaysMenuWithCategories() throws ServletException, IOException {
        // Arrange
        Category cat = new Category();
        cat.setId(1);
        cat.setName("Drinks");
        
        when(categoryService.findAll()).thenReturn(Arrays.asList(cat));
        when(request.getParameter("categoryId")).thenReturn(null); // Default
        when(productService.findByCategory(1)).thenReturn(Arrays.asList());
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq("categoryList"), anyList());
        verify(request).setAttribute(eq("productList"), anyList());
        verify(request).setAttribute("selectedCategoryId", 1);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_SelectedCategory() throws ServletException, IOException {
        // Arrange
        Category cat1 = new Category(); cat1.setId(1);
        Category cat2 = new Category(); cat2.setId(2);
        
        when(categoryService.findAll()).thenReturn(Arrays.asList(cat1, cat2));
        when(request.getParameter("categoryId")).thenReturn("2");
        when(productService.findByCategory(2)).thenReturn(Arrays.asList());
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute("selectedCategoryId", 2);
        verify(productService).findByCategory(2);
    }
}
