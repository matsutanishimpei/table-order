package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 * ログインチェックおよび権限チェックを行うフィルタです。
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getServletPath();

        // ログイン画面、画像、CSSなどは除外
        if (path.equals("/Login") || path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");

        // CSRF トークン管理
        String sessionToken = (String) session.getAttribute("csrf_token");
        if (sessionToken == null) {
            sessionToken = util.CsrfUtil.generateToken();
            session.setAttribute("csrf_token", sessionToken);
        }

        // ログイン、画像などは除外
        if (path.equals("/Login") || path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) {
            chain.doFilter(request, response);
            return;
        }

        // ログインチェック
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/Login");
            return;
        }

        // CSRF 検証 (POST)
        if (req.getMethod().equalsIgnoreCase("POST")) {
            String requestToken = request.getParameter("csrf_token");
            if (!util.CsrfUtil.validateToken(requestToken, sessionToken)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF保護エラー");
                return;
            }
        }

        // 権限チェック
        boolean authorized = true;
        if (path.startsWith("/Admin/") && !user.isAdmin()) {
            authorized = false;
        } else if (path.startsWith("/Kitchen/") && !user.isKitchen() && !user.isAdmin()) {
            authorized = false;
        } else if (path.startsWith("/Hall/") && !user.isHall() && !user.isAdmin()) {
            authorized = false;
        } else if (path.startsWith("/Cashier/") && !user.isCashier() && !user.isAdmin()) {
            authorized = false;
        }

        if (!authorized) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "アクセス権限がありません。");
            return;
        }

        chain.doFilter(request, response);
    }
}
