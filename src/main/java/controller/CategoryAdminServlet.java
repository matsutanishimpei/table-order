package controller;

import java.io.IOException;
import java.util.List;

import database.CategoryDAO;
import database.impl.CategoryDAOImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;

/**
 * 管理者用のカテゴリ管理を制御するサーブレットです。
 */
@WebServlet("/Admin/Category")
public class CategoryAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CategoryDAO categoryDAO = new CategoryDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> list = categoryDAO.findAll();
        request.setAttribute("categoryList", list);
        request.getRequestDispatcher("/WEB-INF/view/admin_categories.jsp").forward(request, response);
    }
}
