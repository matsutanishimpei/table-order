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
import jakarta.servlet.http.HttpSession;
import model.TableOrderSummary;
import model.User;

/**
 * 座席ごとの注文履歴を表示するサーブレットです。
 */
@WebServlet("/OrderHistory")
public class OrderHistory extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderService orderService = new OrderServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getTableId() == null) {
            response.sendRedirect("Login");
            return;
        }

        TableOrderSummary summary = orderService.getTableOrderSummary(user.getTableId());
        
        request.setAttribute("summary", summary);
        request.getRequestDispatcher("/WEB-INF/view/order_history.jsp").forward(request, response);
    }
}
