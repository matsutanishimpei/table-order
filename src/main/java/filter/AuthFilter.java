package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.CsrfUtil;

/**
 * 認証・認可およびCSRF対策を行うフィルタークラスです。
 */
@WebFilter(urlPatterns = {"/Admin/*", "/Kitchen/*", "/Hall/*", "/Cashier/*", "/Order", "/Cart", "/OrderHistory"})
public class AuthFilter implements Filter {

    public void init(FilterConfig fConfig) throws ServletException {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        String path = req.getServletPath();

        // 1. ログインチェック
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/Login");
            return;
        }

        User user = (User) session.getAttribute("user");

        String sessionToken = (String) session.getAttribute("csrf_token");
        if (sessionToken == null) {
            sessionToken = CsrfUtil.generateToken();
            session.setAttribute("csrf_token", sessionToken);
        }
        // JSPから ${csrf_token} で参照できるようにリクエスト属性にセット
        req.setAttribute("csrf_token", sessionToken);

        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String requestToken = req.getParameter("csrf_token");
            
            // トークンが送信されていない、または一致しない場合はエラー
            if (!CsrfUtil.validateToken(requestToken, sessionToken)) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "不正なリクエストです（CSRFトークンエラー）。");
                return;
            }
            
            // トークンをローテーション（使用済みトークンを無効化）
            String newToken = CsrfUtil.generateToken();
            session.setAttribute("csrf_token", newToken);
        }

        // 3. 権限（認可）チェック
        if (path.startsWith("/Admin/") && !user.isAdmin()) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "アクセス権限がありません。");
            return;
        } else if (path.startsWith("/Kitchen/") && !user.isKitchen() && !user.isAdmin()) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "アクセス権限がありません。");
            return;
        } else if (path.startsWith("/Hall/") && !user.isHall() && !user.isAdmin()) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "アクセス権限がありません。");
            return;
        } else if (path.startsWith("/Cashier/") && !user.isCashier() && !user.isAdmin()) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "アクセス権限がありません。");
            return;
        }

        // 全てのチェックを通過したら次の処理へ
        chain.doFilter(request, response);
    }

    public void destroy() {}
}
