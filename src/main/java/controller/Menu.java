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

/**
 * メニュー画面を表示するサーブレットクラスです。
 */
@WebServlet("/Menu")
public class Menu extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // セッションの確認（ログインしていない場合はログイン画面に弾く）
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("Login");
            return;
        }

        // DAOの準備
        CategoryDAO cDao = new CategoryDAO();
        ProductDAO pDao = new ProductDAO();

        // カテゴリ一覧の取得
        List<Category> categories = cDao.findAll();
        request.setAttribute("categories", categories);

        // 表示するカテゴリIDの取得（未指定なら最初のカテゴリ）
        String cIdStr = request.getParameter("categoryId");
        int categoryId = (cIdStr != null) ? Integer.parseInt(cIdStr) : (categories.isEmpty() ? 0 : categories.get(0).getId());
        
        // 指定カテゴリの商品一覧を取得
        List<Product> products = pDao.findByCategory(categoryId);
        request.setAttribute("products", products);
        request.setAttribute("selectedCategoryId", categoryId);

        // メニュー画面にフォワード
        request.getRequestDispatcher("/WEB-INF/view/menu.jsp").forward(request, response);
    }
}
