package controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TableOrderSummary;
import service.OrderService;
import service.ServiceFactory;
import service.TableService;

import util.AppConstants;

/**
 * 会計（レジ）用の管理サーブレットです。
 */
@WebServlet("/Cashier/Home")
public class CashierServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final OrderService orderService;
    private final TableService tableService;

    public CashierServlet() {
        this(ServiceFactory.getOrderService(), ServiceFactory.getTableService());
    }

    public CashierServlet(OrderService orderService, TableService tableService) {
        this.orderService = orderService;
        this.tableService = tableService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 未精算の座席一覧を取得
        List<TableOrderSummary> tables = tableService.findUnsettledTables();
        request.setAttribute(AppConstants.ATTR_UNSETTLED_TABLES, tables);

        // 特定の座席が選択されている場合、その詳細を取得
        String tableIdStr = request.getParameter("tableId");
        int tableId = util.ValidationUtil.parseIntSafe(tableIdStr, 0);

        if (tableId > 0) {
            Optional<TableOrderSummary> summaryOpt = tableService.getTableOrderSummary(tableId);
            summaryOpt.ifPresent(summary -> request.setAttribute(AppConstants.ATTR_SELECTED_SUMMARY, summary));
        }

        request.getRequestDispatcher(AppConstants.VIEW_CASHIER).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("checkout".equals(action)) {
            // 会計完了処理
            int tableId = util.ValidationUtil.parseIntSafe(request.getParameter("tableId"), 0);
            if (tableId > 0) {
                boolean success = orderService.completeCheckout(tableId);
                if (!success) {
                    request.setAttribute(AppConstants.ATTR_ERROR, "会計処理に失敗しました。未提供の商品が残っている可能性があります。");
                    doGet(request, response);
                    return;
                }
            }
        }

        response.sendRedirect(AppConstants.REDIRECT_HOME);
    }
}
