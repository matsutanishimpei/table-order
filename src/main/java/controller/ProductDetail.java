package controller;

import java.io.IOException;

import service.ProductService;
import service.impl.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;

/**
 * 商品詳細画面を制御するサーブレットです。
 */
@WebServlet("/ProductDetail")
public class ProductDetail extends HttpServlet {
    private final ProductService productService;

    public ProductDetail() {
        this(new ProductServiceImpl());
    }

    public ProductDetail(ProductService productService) {
        this.productService = productService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = util.ValidationUtil.parseIntSafe(request.getParameter("productId"), -1);
        if (productId < 0) {
            response.sendRedirect("Menu");
            return;
        }

        Product p = productService.findById(productId);

        if (p == null) {
            response.sendRedirect("Menu");
            return;
        }

        request.setAttribute("product", p);
        request.getRequestDispatcher("/WEB-INF/view/product_detail.jsp").forward(request, response);
    }
}
