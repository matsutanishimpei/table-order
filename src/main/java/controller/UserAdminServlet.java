package controller;

import java.io.IOException;
import java.util.List;

import service.OrderService;
import service.impl.OrderServiceImpl;
import service.UserService;
import service.impl.UserServiceImpl;

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
    private final UserService userService = new UserServiceImpl();
    private final OrderService orderService = new OrderServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("edit".equals(action) || "add".equals(action)) {
            // 編集または新規登録画面
            String id = request.getParameter("id");
            User targetUser = new User();
            if ("edit".equals(action) && id != null) {
                targetUser = userService.findById(id);
            }
            // セッションの user と競合しないよう targetUser という名前でセット
            request.setAttribute("targetUser", targetUser);
            
            // 空席確認（テーブル番号選択用）
            List<model.TableStatusView> tables = orderService.findAllTableStatus();
            request.setAttribute("tables", tables);
            
            request.getRequestDispatcher("/WEB-INF/view/admin_user_edit.jsp").forward(request, response);
        } else {
            // 一覧表示
            List<User> list = userService.findAll();
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
            userService.delete(id);
        } else if ("update".equals(action)) {
            userService.update(targetUser, password);
        } else {
            // 新規追加
            targetUser.setPassword(password);
            userService.register(targetUser);
        }

        response.sendRedirect("User");
    }
}
