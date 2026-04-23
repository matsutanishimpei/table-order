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
import jakarta.servlet.http.HttpSession;
import model.Category;
import model.Product;

/**
 * メニュー画面を表示するサーブレットクラスです。
 */
@WebServlet("/Menu")
public class Menu extends HttpServlet {
    private final CategoryService categoryService;
    private final ProductService productService;

    public Menu() {
        this(new CategoryServiceImpl(), new ProductServiceImpl());
    }

    public Menu(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // セッションの確認（ログインしていない場合はログイン画面に弾く）
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("Login");
            return;
        }

        // カテゴリ一覧の取得
        List<Category> categories = categoryService.findAll();
        request.setAttribute("categories", categories);

        // 表示するカテゴリIDの取得（未指定なら最初のカテゴリを表示）
        int categoryId = util.ValidationUtil.parseIntSafe(request.getParameter("categoryId"),
                          categories.isEmpty() ? 0 : categories.get(0).getId());
        
        // 当該カテゴリの商品一覧を取得
        List<Product> products = productService.findByCategory(categoryId);
        request.setAttribute("products", products);
        request.setAttribute("selectedCategoryId", categoryId);

        // メニュー画面にフォワード
        request.getRequestDispatcher("/WEB-INF/view/menu.jsp").forward(request, response);
    }
}
