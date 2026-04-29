package filter;

import static org.mockito.Mockito.*;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.AppConstants;

@ExtendWith(MockitoExtension.class)
class AuthFilterTest {

    private AuthFilter filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new AuthFilter();
    }

    @Test
    @DisplayName("非HTTPリクエストの場合はチェックをスキップして通過すること")
    void doFilter_NotHttpServletRequest_PassesThrough() throws IOException, ServletException {
        ServletRequest req = mock(ServletRequest.class);
        ServletResponse res = mock(ServletResponse.class);

        filter.doFilter(req, res, chain);

        verify(chain).doFilter(req, res);
    }

    @Test
    @DisplayName("未ログイン（セッションなし）の場合はログイン画面へリダイレクトされること")
    void doFilter_NoSession_RedirectsToLogin() throws IOException, ServletException {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/table-order");

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect(contains(AppConstants.REDIRECT_LOGIN));
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    @DisplayName("ログイン済み(GET): CSRFトークンがリクエストにセットされ通過すること")
    void doFilter_LoggedIn_SetsCsrfToken() throws IOException, ServletException {
        User user = new User("admin", "pass", 1, null);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(request.getServletPath()).thenReturn("/Admin/Home");
        when(request.getMethod()).thenReturn("GET");

        filter.doFilter(request, response, chain);

        verify(session).setAttribute(eq(AppConstants.ATTR_CSRF_TOKEN), anyString());
        verify(request).setAttribute(eq(AppConstants.ATTR_CSRF_TOKEN), anyString());
        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("POSTリクエスト: 有効なCSRFトークンがあれば通過し、トークンが更新されること")
    void doFilter_PostValidCsrf_Passes() throws IOException, ServletException {
        User user = new User("admin", "pass", 1, null);
        String token = "valid-token";
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(session.getAttribute(AppConstants.ATTR_CSRF_TOKEN)).thenReturn(token);
        when(request.getServletPath()).thenReturn("/Admin/Home");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter(AppConstants.ATTR_CSRF_TOKEN)).thenReturn(token);

        filter.doFilter(request, response, chain);

        // トークンがセット（初期セットと更新用セットの2回以上呼ばれるはず）
        verify(session, atLeast(1)).setAttribute(eq(AppConstants.ATTR_CSRF_TOKEN), anyString());
        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("POSTリクエスト: 不正なCSRFトークンの場合は400エラーになること")
    void doFilter_PostInvalidCsrf_Returns400() throws IOException, ServletException {
        User user = new User("admin", "pass", 1, null);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(session.getAttribute(AppConstants.ATTR_CSRF_TOKEN)).thenReturn("session-token");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter(AppConstants.ATTR_CSRF_TOKEN)).thenReturn("wrong-token");

        filter.doFilter(request, response, chain);

        verify(response).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), anyString());
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    @DisplayName("認可チェック: 管理者パスに一般ユーザーがアクセスした場合は403エラーになること")
    void doFilter_AdminPath_NoPermission() throws IOException, ServletException {
        User user = new User("kitchen1", "pass", 2, null); // Kitchen role
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(request.getServletPath()).thenReturn("/Admin/Users");
        when(request.getMethod()).thenReturn("GET");

        filter.doFilter(request, response, chain);

        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    @DisplayName("認可チェック: キッチンパスにキッチン担当がアクセスした場合は通過すること")
    void doFilter_KitchenPath_Success() throws IOException, ServletException {
        User user = new User("k1", "pass", 2, null); // Kitchen role
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(request.getServletPath()).thenReturn("/Kitchen/Orders");
        when(request.getMethod()).thenReturn("GET");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("認可チェック: ホールパスにホール担当がアクセスした場合は通過すること")
    void doFilter_HallPath_Success() throws IOException, ServletException {
        User user = new User("h1", "pass", 3, null); // Hall role
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(request.getServletPath()).thenReturn("/Hall/Home");
        when(request.getMethod()).thenReturn("GET");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("認可チェック: レジパスにレジ担当がアクセスした場合は通過すること")
    void doFilter_CashierPath_Success() throws IOException, ServletException {
        User user = new User("c1", "pass", 4, null); // Cashier role
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(request.getServletPath()).thenReturn("/Cashier/Home");
        when(request.getMethod()).thenReturn("GET");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("認可チェック: キッチン担当がホールパスにアクセスした場合は403エラーになること")
    void doFilter_HallPath_Forbidden() throws IOException, ServletException {
        User user = new User("k1", "pass", 2, null); // Kitchen role
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(request.getServletPath()).thenReturn("/Hall/Home");
        when(request.getMethod()).thenReturn("GET");

        filter.doFilter(request, response, chain);

        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    @DisplayName("認可チェック: キッチン担当がレジパスにアクセスした場合は403エラーになること")
    void doFilter_CashierPath_Forbidden() throws IOException, ServletException {
        User user = new User("k1", "pass", 2, null); // Kitchen role
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(request.getServletPath()).thenReturn("/Cashier/Home");
        when(request.getMethod()).thenReturn("GET");

        filter.doFilter(request, response, chain);

        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    @DisplayName("認可チェック: 管理者がキッチンパスにアクセスした場合は通過すること")
    void doFilter_KitchenPath_AdminSuccess() throws IOException, ServletException {
        User user = new User("admin", "pass", 1, null); // Admin role
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(AppConstants.ATTR_USER)).thenReturn(user);
        when(request.getServletPath()).thenReturn("/Kitchen/Orders");
        when(request.getMethod()).thenReturn("GET");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }
}
