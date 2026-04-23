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
 * キッチンモニター画面を制御するサーブレットです。
 */
@WebServlet("/Kitchen/Home")
public class KitchenServlet extends HttpServlet {
    private final OrderService orderService;

    public KitchenServlet() {
        this(new OrderServiceImpl());
    }

    public KitchenServlet(OrderService orderService) {
        this.orderService = orderService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<model.OrderItemView> orders = orderService.findActiveOrderItems();
        request.setAttribute("activeItems", orders);
        request.getRequestDispatcher("/WEB-INF/view/kitchen.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int itemId = util.ValidationUtil.parseIntSafe(request.getParameter("itemId"), -1);
        if (itemId < 0) {
            response.sendRedirect("Home");
            return;
        }

        String action = request.getParameter("action");
        if ("complete".equals(action)) {
            orderService.updateItemStatus(itemId, model.OrderConstants.STATUS_COOKING_DONE);
        }
        response.sendRedirect("Home");
    }
}
