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

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // 非ログイン状態
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/Login");
            return;
        }

        // パスに応じた権限チェック
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
