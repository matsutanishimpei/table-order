package controller;

import java.io.IOException;
import java.util.List;

import service.CategoryService;
import service.impl.CategoryServiceImpl;
import service.ProductService;
import service.impl.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import model.Product;

/**
 * 管理者用の商品管理を制御するサーブレットです。
 */
@WebServlet("/Admin/Product")
public class ProductAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductAdminServlet() {
        this(new ProductServiceImpl(), new CategoryServiceImpl());
    }

    public ProductAdminServlet(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = productService.findAll();
        List<Category> categories = categoryService.findAll();

        request.setAttribute("productList", products);
        request.setAttribute("categoryList", categories);

        request.getRequestDispatcher("/WEB-INF/view/admin_products.jsp").forward(request, response);
    }
}
