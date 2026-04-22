package controller;

import java.io.IOException;
import java.util.List;

import database.UserDAO;
import database.OrderDAO;
import database.impl.UserDAOImpl;
import database.impl.OrderDAOImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import model.TableStatusView;

/**
 * 管理者用のユーザー管理（一覧・追加・更新・削除）を制御するサーブレットです。
 */
@WebServlet("/Admin/User")
public class UserAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = new UserDAOImpl();
    private final OrderDAO orderDAO = new OrderDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("edit".equals(action) || "add".equals(action)) {
            // 編集または新規登録画面
            String id = request.getParameter("id");
            User targetUser = new User();
            if ("edit".equals(action) && id != null) {
                targetUser = userDAO.findById(id);
            }
            // セッションの user と競合しないよう targetUser という名前でセット
            request.setAttribute("targetUser", targetUser);
            
            // 空席確認（テーブル番号選択用）
            List<TableStatusView> tables = orderDAO.findAllTableStatus();
            request.setAttribute("tables", tables);
            
            request.getRequestDispatcher("/WEB-INF/view/admin_user_edit.jsp").forward(request, response);
        } else {
            // 一覧表示
            List<User> list = userDAO.findAll();
            request.setAttribute("userList", list);
            request.getRequestDispatcher("/WEB-INF/view/admin_users.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        int role = util.ValidationUtil.parseIntSafe(request.getParameter("role"), -1);
        if (role < 0) {
            response.sendRedirect("User");
            return;
        }
        String tableIdStr = request.getParameter("tableId");
        Integer tableId = (tableIdStr == null || tableIdStr.isEmpty() || "0".equals(tableIdStr)) ? null : Integer.valueOf(util.ValidationUtil.parseIntSafe(tableIdStr, 0));

        User targetUser = new User();
        targetUser.setId(id);
        targetUser.setRole(role);
        targetUser.setTableId(tableId);

        if ("delete".equals(action)) {
            userDAO.delete(id);
        } else if ("update".equals(action)) {
            userDAO.update(targetUser);
            // パスワードが入力されていれば更新
            if (password != null && !password.isEmpty()) {
                userDAO.updatePassword(id, password);
            }
        } else {
            // 新規追加
            targetUser.setPassword(password);
            userDAO.insert(targetUser);
        }

        response.sendRedirect("User");
    }
}
