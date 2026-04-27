package controller;

import java.io.IOException;
import java.util.Optional;

import service.UserService;
import service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import model.User;
import util.AppConstants;
import util.CsrfUtil;

/**
 * ログイン処理を制御するサーブレットクラスです。
 */
@WebServlet("/Login")
@Slf4j
public class LoginServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
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
        session.setAttribute(AppConstants.ATTR_CSRF_TOKEN, token);

        request.getRequestDispatcher(AppConstants.VIEW_LOGIN).forward(request, response);
    }

    /**
     * ログイン認証を実行します。
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CSRF トークンの検証（AuthFilter 対象外のため自前で実施）
        HttpSession session = request.getSession(false);
        String sessionToken = (session != null) ? (String) session.getAttribute(AppConstants.ATTR_CSRF_TOKEN) : null;
        String requestToken = request.getParameter(AppConstants.ATTR_CSRF_TOKEN);
        if (!CsrfUtil.validateToken(requestToken, sessionToken)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "不正なリクエストです（CSRFトークンエラー）。");
            return;
        }

        // パラメータ取得
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        // バリデーション
        util.ValidationResult vr = util.ValidationUtil.validateRequired(id, "ユーザーID");
        if (vr.isValid()) {
            vr = util.ValidationUtil.validateRequired(pw, "パスワード");
        }

        if (vr.isInvalid()) {
            request.setAttribute(AppConstants.ATTR_ERROR, vr.getMessage());
            request.getRequestDispatcher(AppConstants.VIEW_LOGIN).forward(request, response);
            return;
        }

        Optional<User> userOpt = userService.login(id, pw);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // ログイン成功時：セッション固定攻撃対策としてIDを更新
            request.changeSessionId();
            
            // セッションにユーザー情報を保存してリダイレクト
            session = request.getSession();
            session.setAttribute(AppConstants.ATTR_USER, user);
            
            if (user.isAdmin()) {
                response.sendRedirect(AppConstants.REDIRECT_ADMIN_HOME);
            } else if (user.isKitchen()) {
                response.sendRedirect(AppConstants.REDIRECT_KITCHEN_HOME);
            } else if (user.isHall()) {
                response.sendRedirect(AppConstants.REDIRECT_HALL_HOME);
            } else if (user.isCashier()) {
                response.sendRedirect(AppConstants.REDIRECT_CASHIER_HOME);
            } else {
                response.sendRedirect(AppConstants.REDIRECT_MENU);
            }
        } else {
            // ログイン失敗時
            log.warn("ログイン失敗試行: ID={}", id);
            request.setAttribute(AppConstants.ATTR_ERROR, "ユーザーIDまたはパスワードが正しくありません。");
            request.getRequestDispatcher(AppConstants.VIEW_LOGIN).forward(request, response);
        }
    }
}
