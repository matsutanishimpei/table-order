package controller;

import java.io.IOException;
import database.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;

/**
 * 特定の商品の詳細情報を表示するサーブレットです。
 */
@WebServlet("/ProductDetail")
public class ProductDetail extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // セッション確認
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("Login");
            return;
        }

        // パラメータ取得
        int productId = util.ValidationUtil.parseIntSafe(request.getParameter("productId"), -1);
        String categoryId = request.getParameter("categoryId");

        if (productId <= 0) {
            response.sendRedirect("Menu" + (categoryId != null ? "?categoryId=" + categoryId : ""));
            return;
        }

        // 商品データ取得
        ProductDAO pDao = new ProductDAO();
        Product product = pDao.findById(productId);

        if (product == null) {
            response.sendRedirect("Menu" + (categoryId != null ? "?categoryId=" + categoryId : ""));
            return;
        }

        request.setAttribute("product", product);
        request.setAttribute("selectedCategoryId", categoryId);

        // 詳細画面へフォワード
        request.getRequestDispatcher("/WEB-INF/view/product_detail.jsp").forward(request, response);
    }
}
