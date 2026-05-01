package controller;

import java.io.IOException;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import service.ServiceFactory;
import service.TableService;
import service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import util.AppConstants;

/**
 * 管理者用のユーザー管理（一覧・追加・更新・削除）を制御するサーブレットです。
 */
@WebServlet("/Admin/User")
public class UserAdminServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final UserService userService;

    @SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final TableService tableService;

    public UserAdminServlet() {
        this(ServiceFactory.getUserService(), ServiceFactory.getTableService());
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public UserAdminServlet(UserService userService, TableService tableService) {
        this.userService = userService;
        this.tableService = tableService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 権限チェック：管理者権限がない場合は 403 エラー
        model.User user = (model.User) request.getSession().getAttribute(AppConstants.ATTR_USER);
        if (user == null || !user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");

        if ("edit".equals(action) || "add".equals(action)) {
            // 編集または新規登録画面
            String id = request.getParameter("id");
            User targetUser = new User();
            if ("edit".equals(action) && id != null) {
                targetUser = userService.findById(id).orElse(new User());
            }
            // セッションの user と競合しないよう targetUser という名前でセット
            request.setAttribute("targetUser", targetUser);
            
            // 空席確認（テーブル番号選択用）
            List<model.TableStatusView> tables = tableService.findAllTableStatus();
            request.setAttribute(AppConstants.ATTR_TABLE_LIST, tables);
            
            request.getRequestDispatcher(AppConstants.VIEW_ADMIN_USER_EDIT).forward(request, response);
        } else {
            // 一覧表示
            List<User> list = userService.findAll();
            request.setAttribute(AppConstants.ATTR_USER_LIST, list);
            request.getRequestDispatcher(AppConstants.VIEW_ADMIN_USERS).forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 権限チェック：管理者権限がない場合は 403 エラー
        model.User user = (model.User) request.getSession().getAttribute(AppConstants.ATTR_USER);
        if (user == null || !user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        int role = util.ValidationUtil.parseIntSafe(request.getParameter("role"), -1);
        if (role < 0) {
            response.sendRedirect(AppConstants.REDIRECT_ADMIN_USER);
            return;
        }
        String tableIdStr = request.getParameter("tableId");
        int parsedTableId = util.ValidationUtil.parseIntSafe(tableIdStr, 0);
        Integer tableId = (parsedTableId <= 0) ? null : parsedTableId;

        try {
            if ("delete".equals(action)) {
                userService.delete(id, user.id());
            } else if ("update".equals(action)) {
                User targetUser = new User(id, null, role, tableId, false);
                userService.update(targetUser, password, user.id());
            } else {
                // 新規追加
                User targetUser = new User(id, password, role, tableId, false);
                userService.register(targetUser, user.id());
            }
            response.sendRedirect(AppConstants.REDIRECT_ADMIN_USER);
        } catch (exception.BusinessException e) {
            request.setAttribute(AppConstants.ATTR_ERROR, e.getMessage());
            User targetUser = new User(id, null, role, tableId, false);
            request.setAttribute("targetUser", targetUser);
            List<model.TableStatusView> tables = tableService.findAllTableStatus();
            request.setAttribute(AppConstants.ATTR_TABLE_LIST, tables);
            request.getRequestDispatcher(AppConstants.VIEW_ADMIN_USER_EDIT).forward(request, response);
        }
    }
}
