package controller;

import java.io.IOException;
import java.util.List;

import service.CategoryService;
import service.impl.CategoryServiceImpl;
import service.ProductService;
import service.impl.ProductServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import model.Product;

import util.AppConstants;

/**
 * メニュー画面を表示するサーブレットクラスです。
 */
@WebServlet("/Menu")
public class MenuServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final CategoryService categoryService;
    private final ProductService productService;

    public MenuServlet() {
        this(new CategoryServiceImpl(), new ProductServiceImpl());
    }

    public MenuServlet(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // カテゴリ一覧の取得
        List<Category> categories = categoryService.findAll();
        request.setAttribute(AppConstants.ATTR_CATEGORY_LIST, categories);

        // 表示するカテゴリIDの取得（未指定なら最初のカテゴリを表示）
        int categoryId = util.ValidationUtil.parseIntSafe(request.getParameter("categoryId"),
                          categories.isEmpty() ? 0 : categories.get(0).getId());
        
        // 当該カテゴリの商品一覧を取得
        List<Product> products = productService.findByCategory(categoryId);
        request.setAttribute(AppConstants.ATTR_PRODUCT_LIST, products);
        request.setAttribute(AppConstants.ATTR_SELECTED_CATEGORY_ID, categoryId);

        // メニュー画面にフォワード
        request.getRequestDispatcher(AppConstants.VIEW_MENU).forward(request, response);
    }
}
