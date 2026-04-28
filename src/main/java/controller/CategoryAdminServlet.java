package controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import service.CategoryService;
import service.ServiceFactory;

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

    @SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final CategoryService categoryService;

    public CategoryAdminServlet() {
        this(ServiceFactory.getCategoryService());
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public CategoryAdminServlet(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String name = request.getParameter("name");
        int id = util.ValidationUtil.parseIntSafe(request.getParameter("id"), -1);

        try {
            if ("update".equals(action)) {
                if (id <= 0) {
                    response.sendRedirect(AppConstants.REDIRECT_ADMIN_CATEGORY);
                    return;
                }
                Category c = new Category(id, name); // Service 内でトリムとバリデーションが行われる
                categoryService.update(c);
            } else {
                // 新規追加
                categoryService.insert(name);
            }
            response.sendRedirect(AppConstants.REDIRECT_ADMIN_CATEGORY + "?msg=success");

        } catch (exception.BusinessException e) {
            handleError(request, response, e.getMessage(), id, name, action);
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String message,
            int id, String name, String action) throws ServletException, IOException {
        request.setAttribute(AppConstants.ATTR_ERROR, message);
        if ("update".equals(action) && id > 0) {
            Category c = new Category(id, name);
            request.setAttribute(AppConstants.ATTR_CATEGORY, c);
            request.getRequestDispatcher(AppConstants.VIEW_ADMIN_CATEGORY_EDIT).forward(request, response);
        } else {
            List<Category> list = categoryService.findAll();
            request.setAttribute(AppConstants.ATTR_CATEGORY_LIST, list);
            request.getRequestDispatcher(AppConstants.VIEW_ADMIN_CATEGORIES).forward(request, response);
        }
    }
}
