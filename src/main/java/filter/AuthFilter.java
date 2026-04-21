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

        // 1. 静的リソースとログイン画面の除外（即時通過）
        if (path.equals("/Login") || path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. セッションの存在チェック (ここでは無条件に生成しない)
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // 3. ログインしていない場合はログイン画面へリダイレクト
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/Login");
            return;
        }

        // --- ここから下は「認証済みユーザーのセッション」であることが確定 ---

        // 4. CSRF トークン管理 (認証済みなので安全に格納可能)
        String sessionToken = (String) session.getAttribute("csrf_token");
        if (sessionToken == null) {
            sessionToken = util.CsrfUtil.generateToken();
            session.setAttribute("csrf_token", sessionToken);
        }

        // 5. CSRF 検証 (POSTのみ)
        if (req.getMethod().equalsIgnoreCase("POST")) {
            String requestToken = request.getParameter("csrf_token");
            if (!util.CsrfUtil.validateToken(requestToken, sessionToken)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF保護エラー");
                return;
            }
        }

        // 6. 行き先パスによる権限チェック
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

        // すべて問題なければ次の処理（Servlet等）へ
        chain.doFilter(request, response);
    }
}
