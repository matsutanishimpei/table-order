package controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    }

    @Test
    public void testDoPost_AddToCart_NewItem() throws ServletException, IOException {
        // Arrange
        List<CartItem> cart = new ArrayList<>();
        Product p = new Product();
        p.setId(101);
        p.setName("Matcha Latte");
        p.setPrice(500);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("productId")).thenReturn("101");
        when(request.getParameter("quantity")).thenReturn("2");
        when(productService.findById(101)).thenReturn(p);
        when(request.getParameter("categoryId")).thenReturn("1");

        // Act
        servlet.doPost(request, response);

        // Assert
        assertEquals(1, cart.size());
        assertEquals("Matcha Latte", cart.get(0).getName());
        assertEquals(2, cart.get(0).getQuantity());
        verify(response).sendRedirect("Menu?categoryId=1");
    }

    @Test
    public void testDoPost_AddToCart_ExistingItem() throws ServletException, IOException {
        // Arrange
        List<CartItem> cart = new ArrayList<>();
        CartItem existingItem = new CartItem();
        existingItem.setProductId(101);
        existingItem.setQuantity(1);
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
        assertEquals(4, cart.get(0).getQuantity()); // 1 + 3
        verify(response).sendRedirect("Menu");
    }

    @Test
    public void testDoPost_ClearCart() throws ServletException, IOException {
        // Arrange
        List<CartItem> cart = new ArrayList<>();
        cart.add(new CartItem());

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart);
        when(request.getParameter("action")).thenReturn("clear");
        when(request.getParameter("categoryId")).thenReturn(null);

        // Act
        servlet.doPost(request, response);

        // Assert
        assertTrue(cart.isEmpty());
        verify(response).sendRedirect("Menu");
    }
}
