package controller;

import java.io.IOException;
import java.util.List;

import service.OrderService;
import service.impl.OrderServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.OrderItemView;

/**
 * 配膳（ホール）モニター画面を制御するサーブレットです。
 */
@WebServlet("/Hall/Home")
public class HallServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final OrderService orderService;

    public HallServlet() {
        this(new OrderServiceImpl());
    }

    public HallServlet(OrderService orderService) {
        this.orderService = orderService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<OrderItemView> orders = orderService.findReadyOrderItems();
        request.setAttribute("readyItems", orders);
        request.getRequestDispatcher("/WEB-INF/view/hall.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int itemId = util.ValidationUtil.parseIntSafe(request.getParameter("itemId"), -1);
        if (itemId < 0) {
            response.sendRedirect("Home");
            return;
        }

        String action = request.getParameter("action");
        if ("serve".equals(action)) {
            orderService.updateItemStatus(itemId, model.OrderConstants.STATUS_SERVED);
        }
        response.sendRedirect("Home");
    }
}
