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
        int itemId = util.ValidationUtil.parseIntSafe(request.getParameter("itemId"), -1);
        if (itemId < 0) {
            response.sendRedirect(AppConstants.REDIRECT_HOME);
            return;
        }

        String action = request.getParameter("action");
        if ("complete".equals(action)) {
            orderService.updateItemStatus(itemId, model.OrderConstants.STATUS_COOKING_DONE);
        }
        response.sendRedirect(AppConstants.REDIRECT_HOME);
    }
}
