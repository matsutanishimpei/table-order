package controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import service.CategoryService;
import service.impl.CategoryServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;

import util.AppConstants;

/**
 * 管理者用のカテゴリ管理を制御するサーブレットです。
 */
@WebServlet("/Admin/Category")
public class CategoryAdminServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final CategoryService categoryService;

    public CategoryAdminServlet() {
        this(new CategoryServiceImpl());
    }

    public CategoryAdminServlet(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            int id = util.ValidationUtil.parseIntSafe(request.getParameter("id"), -1);
            Optional<Category> categoryOpt = categoryService.findById(id);
            if (categoryOpt.isPresent()) {
                request.setAttribute(AppConstants.ATTR_CATEGORY, categoryOpt.get());
                request.getRequestDispatcher(AppConstants.VIEW_ADMIN_CATEGORY_EDIT).forward(request, response);
                return;
            }
        }
        
        List<Category> list = categoryService.findAll();
        request.setAttribute(AppConstants.ATTR_CATEGORY_LIST, list);
        request.getRequestDispatcher(AppConstants.VIEW_ADMIN_CATEGORIES).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String name = request.getParameter("name");
        
        if ("update".equals(action)) {
            int id = util.ValidationUtil.parseIntSafe(request.getParameter("id"), -1);
            if (id > 0 && name != null && !name.trim().isEmpty()) {
                Category c = new Category();
                c.setId(id);
                c.setName(name.trim());
                if (categoryService.update(c)) {
                    response.sendRedirect(AppConstants.REDIRECT_ADMIN_CATEGORY + "?msg=success");
                    return;
                }
            }
            response.sendRedirect(AppConstants.REDIRECT_ADMIN_CATEGORY + "?action=edit&id=" + request.getParameter("id") + "&msg=error");
        } else {
            // 新規追加
            if (name != null && !name.trim().isEmpty()) {
                boolean success = categoryService.insert(name.trim());
                if (success) {
                    response.sendRedirect(AppConstants.REDIRECT_ADMIN_CATEGORY + "?msg=success");
                } else {
                    response.sendRedirect(AppConstants.REDIRECT_ADMIN_CATEGORY + "?msg=error");
                }
            } else {
                response.sendRedirect(AppConstants.REDIRECT_ADMIN_CATEGORY);
            }
        }
    }
}
