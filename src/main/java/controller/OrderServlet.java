package controller;

import java.io.IOException;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import service.OrderService;
import service.ServiceFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CartItem;
import model.User;

import util.AppConstants;

/**
 * 注文の確定処理を行うサーブレットです。
 */
@WebServlet("/Order")
public class OrderServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final OrderService orderService;

    public OrderServlet() {
        this(ServiceFactory.getOrderService());
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public OrderServlet(OrderService orderService) {
        this.orderService = orderService;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // CSRF チェック
        if (!isCsrfTokenValid(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "不正なリクエストです。");
            return;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConstants.ATTR_USER);
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute(AppConstants.ATTR_CART);

        if (user == null || cart == null || cart.isEmpty()) {
            response.sendRedirect(AppConstants.REDIRECT_MENU);
            return;
        }

        // DBに保存
        Integer tableId = user.tableId();
        if (tableId == null) {
            request.setAttribute(AppConstants.ATTR_ERROR, "このアカウントには座席（テーブル番号）が割り当てられていないため、注文できません。");
            request.getRequestDispatcher(AppConstants.VIEW_MENU).forward(request, response);
            return;
        }

        boolean success = orderService.createOrder(tableId, cart, user.id());

        if (success) {
            // カートを空にする
            cart.clear();
            // 完了画面、または完了メッセージ付きのメニュー画面へリダイレクト
            response.sendRedirect(AppConstants.REDIRECT_MENU + "?msg=success");
        } else {
            response.sendRedirect(AppConstants.REDIRECT_MENU + "?msg=error");
        }
    }
}
