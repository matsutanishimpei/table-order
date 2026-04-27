package controller;

import java.io.IOException;
import java.util.Optional;

import service.ProductService;
import service.ServiceFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;

/**
 * 商品詳細画面を制御するサーブレットです。
 */
@WebServlet("/ProductDetail")
public class ProductDetailServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final ProductService productService;

    public ProductDetailServlet() {
        this(ServiceFactory.getProductService());
    }

    public ProductDetailServlet(ProductService productService) {
        this.productService = productService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = util.ValidationUtil.parseIntSafe(request.getParameter("productId"), -1);
        if (productId < 0) {
            response.sendRedirect("Menu");
            return;
        }

        Optional<Product> productOpt = productService.findById(productId);

        if (productOpt.isEmpty()) {
            response.sendRedirect("Menu");
            return;
        }

        request.setAttribute("product", productOpt.get());
        request.getRequestDispatcher("/WEB-INF/view/product_detail.jsp").forward(request, response);
    }
}
