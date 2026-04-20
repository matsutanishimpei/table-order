package controller;

import java.io.IOException;
import java.util.List;

import database.CategoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Category;
import model.User;

/**
 * 管理者用のカテゴリー管理サーブレットです。
 */
@WebServlet("/Admin/Categories")
public class CategoryAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // カテゴリー一覧取得
        CategoryDAO dao = new CategoryDAO();
        List<Category> categoryList = dao.findAll();
        request.setAttribute("categoryList", categoryList);

        // JSPへ
        request.getRequestDispatcher("/WEB-INF/view/admin_categories.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 入力値取得
        String name = request.getParameter("name");

        if (util.ValidationUtil.isNotBlank(name)) {
            // 文字数制限チェック（DB制約：50文字）
            if (!util.ValidationUtil.isWithinLength(name, 50)) {
                response.sendRedirect("Categories?msg=toolong");
                return;
            }

            CategoryDAO dao = new CategoryDAO();
            boolean success = dao.insert(name.trim());
            if (success) {
                response.sendRedirect("Categories?msg=success");
            } else {
                response.sendRedirect("Categories?msg=error");
            }
        } else {
            response.sendRedirect("Categories?msg=empty");
        }
    }
}
