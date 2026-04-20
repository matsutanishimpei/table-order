package controller;

import java.io.IOException;
import java.util.List;

import database.CategoryDAO;
import database.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Category;
import model.Product;
import model.User;

/**
 * 管理者用の商品管理サーブレットです。
 */
@WebServlet("/Admin/Products")
public class ProductAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // データの取得
        ProductDAO pDao = new ProductDAO();
        CategoryDAO cDao = new CategoryDAO();
        
        List<Product> productList = pDao.findAll();
        List<Category> categoryList = cDao.findAll();

        request.setAttribute("productList", productList);
        request.setAttribute("categoryList", categoryList);

        // 管理画面へ
        request.getRequestDispatcher("/WEB-INF/view/admin_products.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // フォームからデータ取得
        String name = request.getParameter("name");
        Integer categoryId = util.ValidationUtil.parseIntOrNull(request.getParameter("categoryId"));
        Integer price = util.ValidationUtil.parseIntOrNull(request.getParameter("price"));
        boolean isAvailable = request.getParameter("isAvailable") != null;

        // バリデーション
        if (!util.ValidationUtil.isNotBlank(name) || categoryId == null || price == null || price < 0) {
            response.sendRedirect("Products?msg=invalid");
            return;
        }
        
        // 文字数制限チェック（DB制約：100文字）
        if (!util.ValidationUtil.isWithinLength(name, 100)) {
            response.sendRedirect("Products?msg=toolong");
            return;
        }

        // モデル作成
        Product p = new Product();
        p.setName(name.trim());
        p.setCategoryId(categoryId);
        p.setPrice(price);
        p.setAvailable(isAvailable);
        p.setImagePath(""); // 今回は無し

        // DAOで保存
        ProductDAO dao = new ProductDAO();
        boolean success = dao.insert(p);

        if (success) {
            response.sendRedirect("Products?msg=success");
        } else {
            response.sendRedirect("Products?msg=error");
        }
    }
}
