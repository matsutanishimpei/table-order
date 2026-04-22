package controller;

import java.io.IOException;

import database.OrderDAO;
import database.impl.OrderDAOImpl;

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
    private final OrderDAO orderDAO = new OrderDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("totalSales", orderDAO.getTotalSales());
        request.setAttribute("dailySales", orderDAO.findDailySales());
        request.setAttribute("productRanking", orderDAO.findProductSalesRanking());

        request.getRequestDispatcher("/WEB-INF/view/admin_sales.jsp").forward(request, response);
    }
}
