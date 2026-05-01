package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CartItem;
import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.ProductService;

@ExtendWith(MockitoExtension.class)
public class CartServletTest {

    private CartServlet servlet;

    @Mock
    private ProductService productService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        servlet = new CartServlet(productService);
        
        // デフォルトで CSRF チェックをパスするように設定
        lenient().when(request.getSession()).thenReturn(session);
        lenient().when(request.getParameter(util.AppConstants.PARAM_CSRF_TOKEN)).thenReturn("valid_token");
        lenient().when(session.getAttribute(util.AppConstants.ATTR_CSRF_TOKEN)).thenReturn("valid_token");
    }

    @Test
    public void testDoPost_AddToCart_NewItem() throws ServletException, IOException {
        // Arrange
        List<CartItem> cart = new ArrayList<>();
        Product p = new Product(101, 1, "Matcha Latte", 500, null, null, null, true, false);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("productId")).thenReturn("101");
        when(request.getParameter("quantity")).thenReturn("2");
        when(productService.findById(101)).thenReturn(Optional.of(p));
        when(request.getParameter("categoryId")).thenReturn("1");

        // Act
        servlet.doPost(request, response);

        // Assert
        assertEquals(1, cart.size());
        assertEquals("Matcha Latte", cart.get(0).name());
        assertEquals(2, cart.get(0).quantity());
        verify(response).sendRedirect("Menu?categoryId=1");
    }

    @Test
    public void testDoPost_AddToCart_ExistingItem() throws ServletException, IOException {
        // Arrange
        List<CartItem> cart = new ArrayList<>();
        CartItem existingItem = new CartItem(101, "Matcha Latte", 500, 1);
        cart.add(existingItem);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("productId")).thenReturn("101");
        when(request.getParameter("quantity")).thenReturn("3");
        when(request.getParameter("categoryId")).thenReturn(null);

        // Act
        servlet.doPost(request, response);

        // Assert
        assertEquals(1, cart.size());
        assertEquals(4, cart.get(0).quantity()); // 1 + 3
        verify(response).sendRedirect("Menu");
    }

    @Test
    public void testDoGet_ForwardsToMenu() throws ServletException, IOException {
        // Arrange
        jakarta.servlet.RequestDispatcher rd = mock(jakarta.servlet.RequestDispatcher.class);
        when(request.getRequestDispatcher(util.AppConstants.VIEW_MENU)).thenReturn(rd);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(rd).forward(request, response);
    }

    @Test
    public void testDoPost_AddToCart_InvalidParams() throws ServletException, IOException {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("productId")).thenReturn("-1");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).sendRedirect(util.AppConstants.REDIRECT_MENU);
    }

    @Test
    public void testDoPost_ClearCart_WithCategoryId() throws ServletException, IOException {
        // Arrange
        List<CartItem> cart = new ArrayList<>();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart);
        when(request.getParameter("action")).thenReturn("clear");
        when(request.getParameter("categoryId")).thenReturn("2");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).sendRedirect(contains("categoryId=2"));
    }

    @Test
    public void testDoPost_NullCartInSession_InitializesCart() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(null);
        when(request.getParameter("action")).thenReturn(null);

        servlet.doPost(request, response);

        verify(session).setAttribute(eq("cart"), anyList());
    }

    @Test
    public void testDoPost_AddToCart_ProductNotFound() throws ServletException, IOException {
        List<CartItem> cart = new ArrayList<>();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("productId")).thenReturn("999");
        when(request.getParameter("quantity")).thenReturn("1");
        when(productService.findById(999)).thenReturn(Optional.empty());

        servlet.doPost(request, response);

        assertTrue(cart.isEmpty());
    }

    @Test
    public void testDoPost_CsrfFailure() throws ServletException, IOException {
        // CSRF トークン不一致
        when(request.getParameter(util.AppConstants.PARAM_CSRF_TOKEN)).thenReturn("wrong_token");
        when(session.getAttribute(util.AppConstants.ATTR_CSRF_TOKEN)).thenReturn("valid_token");

        servlet.doPost(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
    }
}
