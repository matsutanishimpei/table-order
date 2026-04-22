package controller;

import java.io.IOException;
import java.util.List;

import database.OrderDAO;
import database.impl.OrderDAOImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TableStatusView;

/**
 * 全テーブルの状態を一覧表示するモニター用サーブレットです。
 */
@WebServlet("/Admin/Monitor")
public class OrderMonitorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderDAO orderDAO = new OrderDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TableStatusView> tables = orderDAO.findAllTableStatus();
        request.setAttribute("tableList", tables);
        request.getRequestDispatcher("/WEB-INF/view/admin_order_monitor.jsp").forward(request, response);
    }
}
