package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全てのレスポンスにセキュリティヘッダーを付与するフィルタです。
 * ブラウザ側の脆弱性（クリックジャッキング、XSS、MIMEスニッフィング等）を軽減します。
 */
@WebFilter("/*")
public class SecurityHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (response instanceof HttpServletResponse httpResponse) {
            // MIMEスニッフィング対策: ブラウザがコンテンツの内容からMIMEタイプを推測するのを防ぐ
            httpResponse.setHeader("X-Content-Type-Options", "nosniff");

            // クリックジャッキング対策: 他のサイトの iframe 内で表示されるのを防ぐ
            httpResponse.setHeader("X-Frame-Options", "SAMEORIGIN");

            // XSS対策: ブラウザのXSSフィルタを有効にする（モダンブラウザではCSPが推奨されるが、互換性のために付与）
            httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

            // Content Security Policy (CSP): 許可されたソースのみからスクリプト等の読み込みを許可する
            // インラインスクリプト（style等）や外部フォント（Google Fonts）の使用に合わせて調整
            httpResponse.setHeader("Content-Security-Policy",
                    "default-src 'self'; "
                    + "script-src 'self' 'unsafe-inline'; " // JSP内でのインラインスクリプト許可（本来は排除が望ましい）
                    + "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; "
                    + "font-src 'self' https://fonts.gstatic.com; "
                    + "img-src 'self' data: https://res.cloudinary.com; " // Cloudinary 許可
                    + "frame-ancestors 'self';");

            // Referrer-Policy: 外部サイトへ遷移する際のリファラ情報を制御
            httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

            // HSTS: 強制的に HTTPS 通信を行わせる（本番環境想定）
            httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void destroy() { }
}
