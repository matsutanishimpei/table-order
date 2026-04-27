package controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.CategoryService;
import service.ProductService;
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
    private Part filePart;

    @Mock
    private jakarta.servlet.RequestDispatcher requestDispatcher;

    @BeforeEach
    public void setUp() {
        servlet = new ProductAdminServlet(productService, categoryService, imageStorageProvider);
    }

    @Test
    public void testDoPost_SuccessWithImageUpload() throws ServletException, IOException {
        // Arrange
        int productId = 1;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setImagePath("v1/old_image");

        when(request.getParameter("id")).thenReturn(String.valueOf(productId));
        when(request.getParameter("name")).thenReturn("New Burger");
        when(request.getParameter("categoryId")).thenReturn("2");
        when(request.getParameter("price")).thenReturn("1200");
        when(request.getParameter("description")).thenReturn("Delicious burger");
        when(request.getParameter("allergyInfo")).thenReturn("Wheat");
        when(request.getParameter("isAvailable")).thenReturn("true");

        when(productService.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productService.update(any(Product.class))).thenReturn(true);

        // 画像アップロードのモック
        when(request.getPart("imageFile")).thenReturn(filePart);
        when(filePart.getSize()).thenReturn(100L);
        when(filePart.getContentType()).thenReturn("image/jpeg");
        when(imageStorageProvider.upload(any(Part.class))).thenReturn("v2/new_image");

        // Act
        servlet.doPost(request, response);

        // Assert
        // 新しい画像がアップロードされ、古い画像が削除されたことを確認
        verify(imageStorageProvider).upload(filePart);
        verify(imageStorageProvider).delete("v1/old_image");
        verify(productService).update(argThat(p -> "v2/new_image".equals(p.getImagePath())));
        verify(response).sendRedirect("Product?msg=success");
    }

    @Test
    public void testDoPost_InvalidImageFormat() throws ServletException, IOException {
        // Arrange
        int productId = 1;
        Product existingProduct = new Product();
        existingProduct.setId(productId);

        when(request.getParameter("id")).thenReturn(String.valueOf(productId));
        when(request.getParameter("name")).thenReturn("Invalid Product");
        when(request.getParameter("categoryId")).thenReturn("1");
        when(request.getParameter("price")).thenReturn("1000");
        when(request.getParameter("description")).thenReturn("");
        when(request.getParameter("allergyInfo")).thenReturn("");
        when(request.getParameter("isAvailable")).thenReturn("true");

        when(productService.findById(productId)).thenReturn(Optional.of(existingProduct));

        // 不正な画像形式（text/plain）
        when(request.getPart("imageFile")).thenReturn(filePart);
        when(filePart.getSize()).thenReturn(100L);
        when(filePart.getContentType()).thenReturn("text/plain");

        // フォワードのモック
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // Act
        servlet.doPost(request, response);

        // Assert
        // アップロードが実行されず、エラーメッセージがセットされてフォワードされることを確認
        verify(imageStorageProvider, never()).upload(any(Part.class));
        verify(request).setAttribute(eq(util.AppConstants.ATTR_ERROR), contains("許可されていないファイル形式"));
        verify(requestDispatcher).forward(request, response);
    }
}
