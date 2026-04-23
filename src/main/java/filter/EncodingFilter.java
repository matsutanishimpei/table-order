package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

/**
 * アプリケーション全体の文字エンコーディングを UTF-8 に設定するフィルタークラスです。
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig fConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // リクエストのエンコーディングを UTF-8 に設定
        request.setCharacterEncoding("UTF-8");
        // 必要に応じてレスポンスのエンコーディングも設定
        response.setContentType("text/html; charset=UTF-8");
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
