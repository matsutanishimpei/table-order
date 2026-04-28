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
 * 配膳（ホール）モニター画面を制御するサーブレットです。
 */
@WebServlet("/Hall/Home")
public class HallServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final OrderService orderService;

    public HallServlet() {
        this(ServiceFactory.getOrderService());
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public HallServlet(OrderService orderService) {
        this.orderService = orderService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<OrderItemView> orders = orderService.findReadyOrderItems();
        request.setAttribute(AppConstants.ATTR_READY_ITEMS, orders);
        request.getRequestDispatcher(AppConstants.VIEW_HALL).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int itemId = util.ValidationUtil.parseIntSafe(request.getParameter("itemId"), -1);
        if (itemId < 0) {
            response.sendRedirect(AppConstants.REDIRECT_HOME);
            return;
        }

        String action = request.getParameter("action");
        if ("serve".equals(action)) {
            orderService.updateItemStatus(itemId, model.OrderConstants.STATUS_SERVED);
        }
        response.sendRedirect(AppConstants.REDIRECT_HOME);
    }
}
