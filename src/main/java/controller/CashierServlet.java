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
import model.TableOrderSummary;
import model.User;
import service.OrderService;
import service.impl.OrderServiceImpl;

/**
 * 会計（レジ）用の管理サーブレットです。
 */
@WebServlet("/Cashier/Home")
public class CashierServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderDAO orderDAO = new OrderDAOImpl();
    private final OrderService orderService = new OrderServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 未精算の座席一覧を取得
        List<TableOrderSummary> tables = orderDAO.findUnsettledTables();
        request.setAttribute("unsettledTables", tables);

        // 特定の座席が選択されている場合、その詳細を取得
        int tableId = util.ValidationUtil.parseIntSafe(request.getParameter("tableId"), -1);
        if (tableId > 0) {
            TableOrderSummary summary = orderDAO.getTableOrderSummary(tableId);
            request.setAttribute("selectedSummary", summary);
        }

        // 会計画面へ
        request.getRequestDispatcher("/WEB-INF/view/cashier.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("checkout".equals(action)) {
            // 会計完了処理
            int tableId = util.ValidationUtil.parseIntSafe(request.getParameter("tableId"), -1);
            if (tableId > 0) {
                orderService.completeCheckout(tableId);
            }
        }

        // 画面をリロード（座席一覧に戻る）
        response.sendRedirect("Home");
    }
}
