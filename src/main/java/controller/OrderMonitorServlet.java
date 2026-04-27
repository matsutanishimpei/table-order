package controller;

import java.io.IOException;
import java.util.List;

import service.ServiceFactory;
import service.TableService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import util.AppConstants;

/**
 * 全テーブルの状態を一覧表示するモニター用サーブレットです。
 */
@WebServlet("/Admin/Monitor")
public class OrderMonitorServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final TableService tableService;

    public OrderMonitorServlet() {
        this(ServiceFactory.getTableService());
    }

    public OrderMonitorServlet(TableService tableService) {
        this.tableService = tableService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<model.TableStatusView> tables = tableService.findAllTableStatus();
        request.setAttribute(AppConstants.ATTR_TABLE_LIST, tables);
        request.getRequestDispatcher(AppConstants.VIEW_ADMIN_ORDER_MONITOR).forward(request, response);
    }
}
