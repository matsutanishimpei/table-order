package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

/**
 * リクエストごとに一意のIDを発行し、ログに付与するためのフィルタです。
 * MDC (Mapped Diagnostic Context) を使用して、スレッドセーフにIDを管理します。
 */
@WebFilter("/*")
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String REQUEST_ID = "request_id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest req) || !(response instanceof HttpServletResponse res)) {
            chain.doFilter(request, response);
            return;
        }

        // リクエストIDの発行（既存のヘッダーがあればそれを引き継ぐことも検討可能）
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(REQUEST_ID, requestId);

        long startTime = System.currentTimeMillis();
        String method = req.getMethod();
        String uri = req.getRequestURI();
        String remoteAddr = req.getRemoteAddr();

        try {
            logger.info("Request Started: {} {} from {}", method, uri, remoteAddr);
            
            chain.doFilter(request, response);
            
            long duration = System.currentTimeMillis() - startTime;
            int status = res.getStatus();
            logger.info("Request Finished: {} {} -> status={}, duration={}ms", method, uri, status, duration);
            
        } finally {
            // MDC のクリア（スレッドプール利用時に別リクエストへIDが漏れるのを防ぐ）
            MDC.remove(REQUEST_ID);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void destroy() { }
}
