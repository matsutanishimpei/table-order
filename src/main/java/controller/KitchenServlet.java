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
import model.OrderItemView;

import util.AppConstants;

/**
 * キッチンモニター画面を制御するサーブレットです。
 */
@WebServlet("/Kitchen/Home")
public class KitchenServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final OrderService orderService;

    public KitchenServlet() {
        this(ServiceFactory.getOrderService());
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public KitchenServlet(OrderService orderService) {
        this.orderService = orderService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<OrderItemView> orders = orderService.findActiveOrderItems();
        request.setAttribute(AppConstants.ATTR_ACTIVE_ITEMS, orders);
        request.getRequestDispatcher(AppConstants.VIEW_KITCHEN).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 権限チェック：キッチン権限がない場合は 403 エラー
        model.User user = (model.User) request.getSession().getAttribute(AppConstants.ATTR_USER);
        if (user == null || !user.isKitchen()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int itemId = util.ValidationUtil.parseIntSafe(request.getParameter("itemId"), -1);
        if (itemId < 0) {
            response.sendRedirect(AppConstants.REDIRECT_HOME);
            return;
        }

        String action = request.getParameter("action");
        if ("complete".equals(action)) {
            boolean success = orderService.updateItemStatus(itemId,
                    model.OrderConstants.STATUS_COOKING_DONE, user.id());
            if (!success) {
                // UXを優先し画面上にはエラーを出さないが、調査用にログを残す
                // （リダイレクト後に最新状態が再取得されるため、既に完了済みの場合はリストから消える）
                System.err.println("[KitchenServlet] Failed to update item status: itemId=" + itemId);
            }
        }
        response.sendRedirect(AppConstants.REDIRECT_HOME);
    }
}
