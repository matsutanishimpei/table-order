package controller;

import java.io.IOException;
import java.util.Optional;
import service.ProductService;
import service.impl.ProductServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;
import util.ValidationUtil;

/**
 * 商品詳細画面を表示するサーブレットクラスです。
 */
@WebServlet("/Product")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductService productService;

    public ProductServlet() {
        this(new ProductServiceImpl());
    }

    public ProductServlet(ProductService productService) {
        this.productService = productService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = ValidationUtil.parseIntSafe(request.getParameter("id"), -1);
        int categoryId = ValidationUtil.parseIntSafe(request.getParameter("categoryId"), 0);

        Optional<Product> productOpt = productService.findById(id);
        if (productOpt.isEmpty()) {
            response.sendRedirect("Menu");
            return;
        }

        request.setAttribute("product", productOpt.get());
        request.setAttribute("selectedCategoryId", categoryId);

        // 商品詳細画面にフォワード
        request.getRequestDispatcher("/WEB-INF/view/product_detail.jsp").forward(request, response);
    }
}
