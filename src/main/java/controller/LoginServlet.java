package controller;

import java.io.IOException;
import java.util.Optional;

import service.UserService;
import service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Logger;
import model.User;
import util.CsrfUtil;

/**
 * ログイン処理を制御するサーブレットクラスです。
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private final UserService userService;

    public LoginServlet() {
        this(new UserServiceImpl());
    }

    public LoginServlet(UserService userService) {
        this.userService = userService;
    }

    /**
     * ログイン画面を表示します。
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ログイン画面用の CSRF トークンを生成してセッションにセット
        HttpSession session = request.getSession();
        String token = CsrfUtil.generateToken();
        session.setAttribute("csrf_token", token);

        request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
    }

    /**
     * ログイン認証を実行します。
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CSRF トークンの検証（AuthFilter 対象外のため自前で実施）
        HttpSession session = request.getSession(false);
        String sessionToken = (session != null) ? (String) session.getAttribute("csrf_token") : null;
        String requestToken = request.getParameter("csrf_token");
        if (!CsrfUtil.validateToken(requestToken, sessionToken)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "不正なリクエストです（CSRFトークンエラー）。");
            return;
        }

        // パラメータ取得
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        Optional<User> userOpt = userService.login(id, pw);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // ログイン成功時：セッション固定攻撃対策としてIDを更新
            request.changeSessionId();
            
            // セッションにユーザー情報を保存してリダイレクト
            session = request.getSession();
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
