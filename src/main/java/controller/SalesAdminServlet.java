package controller;

import java.io.IOException;

import service.OrderService;
import service.impl.OrderServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 管理者用の売上管理画面を制御するサーブレットです。
 */
@WebServlet("/Admin/Sales")
public class SalesAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderService orderService = new OrderServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("totalSales", orderService.getTotalSales());
        request.setAttribute("dailySales", orderService.findDailySales());
        request.setAttribute("productRanking", orderService.findProductSalesRanking());

        request.getRequestDispatcher("/WEB-INF/view/admin_sales.jsp").forward(request, response);
    }
}
