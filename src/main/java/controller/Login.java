package controller;

import java.io.IOException;

import database.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Logger;
import model.User;

/**
 * ログイン処理を制御するサーブレットクラスです。
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(Login.class.getName());

    /**
     * ログイン画面を表示します。
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
    }

    /**
     * ログイン認証を実行します。
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // パラメータ取得
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        // DAOの生成と認証処理
        UserDAO dao = new UserDAO();
        User user = dao.login(id, pw);

        if (user != null) {
            // ログイン成功時：セッション固定攻撃対策としてIDを更新
            request.changeSessionId();
            
            // セッションにユーザー情報を保存してリダイレクト
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            if (user.isAdmin()) {
                response.sendRedirect("Admin/Home");
            } else if (user.isKitchen()) {
                response.sendRedirect("Kitchen/Home");
            } else if (user.isHall()) {
                response.sendRedirect("Hall/Home");
            } else if (user.isCashier()) {
                response.sendRedirect("Cashier/Home");
            } else {
                response.sendRedirect("Menu");
            }
        } else {
            // ログイン失敗時
            logger.warning("ログイン失敗試行: ID=" + id);
            request.setAttribute("error", "ユーザーIDまたはパスワードが正しくありません。");
            request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
        }
    }
}
