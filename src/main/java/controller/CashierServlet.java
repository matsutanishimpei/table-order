package controller;

import java.io.IOException;
import java.util.List;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TableOrderSummary;
import model.User;
import service.OrderService;
import service.impl.OrderServiceImpl;
import service.TableService;
import service.impl.TableServiceImpl;

/**
 * 会計（レジ）用の管理サーブレットです。
 */
@WebServlet("/Cashier/Home")
public class CashierServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderService orderService;
    private final TableService tableService;

    public CashierServlet() {
        this(new OrderServiceImpl(), new TableServiceImpl());
    }

    public CashierServlet(OrderService orderService, TableService tableService) {
        this.orderService = orderService;
        this.tableService = tableService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 未精算の座席一覧を取得
        List<TableOrderSummary> tables = tableService.findUnsettledTables();
        request.setAttribute("unsettledTables", tables);

        // 特定の座席が選択されている場合、その詳細を取得
        String tableIdStr = request.getParameter("tableId");
        int tableId = util.ValidationUtil.parseIntSafe(tableIdStr, 0);

        if (tableId > 0) {
            TableOrderSummary summary = tableService.getTableOrderSummary(tableId);
            request.setAttribute("selectedSummary", summary);
        }

        request.getRequestDispatcher("/WEB-INF/view/cashier.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("checkout".equals(action)) {
            // 会計完了処理
            int tableId = util.ValidationUtil.parseIntSafe(request.getParameter("tableId"), 0);
            if (tableId > 0) {
                boolean success = orderService.completeCheckout(tableId);
                if (!success) {
                    request.setAttribute("error", "会計処理に失敗しました。未提供の商品が残っている可能性があります。");
                    doGet(request, response);
                    return;
                }
            }
        }

        response.sendRedirect("Home");
    }
}
