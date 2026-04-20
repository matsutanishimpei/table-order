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
        String action = request.getParameter("action");
        ProductDAO pDao = new ProductDAO();
        CategoryDAO cDao = new CategoryDAO();
        
        // 編集モードの場合
        if ("edit".equals(action)) {
            Integer id = util.ValidationUtil.parseIntOrNull(request.getParameter("id"));
            if (id != null) {
                Product p = pDao.findById(id);
                if (p != null) {
                    List<model.Category> categoryList = cDao.findAll();
                    request.setAttribute("product", p);
                    request.setAttribute("categoryList", categoryList);
                    request.getRequestDispatcher("/WEB-INF/view/admin_product_edit.jsp").forward(request, response);
                    return;
                }
            }
            response.sendRedirect("Products?msg=notfound");
            return;
        }

        // 通常の一覧表示
        List<Product> productList = pDao.findAll();
        List<Category> categoryList = cDao.findAll();

        request.setAttribute("productList", productList);
        request.setAttribute("categoryList", categoryList);

        // 管理画面へ
        request.getRequestDispatcher("/WEB-INF/view/admin_products.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // フォームからデータ取得
        Integer id = util.ValidationUtil.parseIntOrNull(request.getParameter("id")); // 編集時は存在する
        String name = request.getParameter("name");
        Integer categoryId = util.ValidationUtil.parseIntOrNull(request.getParameter("categoryId"));
        Integer price = util.ValidationUtil.parseIntOrNull(request.getParameter("price"));
        String description = request.getParameter("description");
        String allergyInfo = request.getParameter("allergyInfo");
        boolean isAvailable = request.getParameter("isAvailable") != null;

        // バリデーション
        if (!util.ValidationUtil.isNotBlank(name) || categoryId == null || price == null || price < 0) {
            String redirectUrl = (id == null) ? "Products?msg=invalid" : "Products?action=edit&id=" + id + "&msg=invalid";
            response.sendRedirect(redirectUrl);
            return;
        }
        
        // 文字数制限チェック
        if (!util.ValidationUtil.isWithinLength(name, 100)) {
            String redirectUrl = (id == null) ? "Products?msg=toolong" : "Products?action=edit&id=" + id + "&msg=toolong";
            response.sendRedirect(redirectUrl);
            return;
        }

        // DAOの準備
        ProductDAO dao = new ProductDAO();
        Product p;
        boolean success;

        if (id != null) {
            // 更新（Update）
            p = dao.findById(id);
            if (p == null) {
                response.sendRedirect("Products?msg=notfound");
                return;
            }
            p.setName(name.trim());
            p.setCategoryId(categoryId);
            p.setPrice(price);
            p.setDescription(description != null ? description.trim() : "");
            p.setAllergyInfo(allergyInfo != null ? allergyInfo.trim() : "");
            p.setAvailable(isAvailable);
            success = dao.update(p);
        } else {
            // 新規登録（Insert）
            p = new Product();
            p.setName(name.trim());
            p.setCategoryId(categoryId);
            p.setPrice(price);
            p.setDescription(description != null ? description.trim() : "");
            p.setAllergyInfo(allergyInfo != null ? allergyInfo.trim() : "");
            p.setAvailable(isAvailable);
            p.setImagePath("");
            success = dao.insert(p);
        }

        if (success) {
            String msg = (id == null) ? "success" : "updatesuccess";
            response.sendRedirect("Products?msg=" + msg);
        } else {
            response.sendRedirect("Products?msg=error");
        }
    }
}
