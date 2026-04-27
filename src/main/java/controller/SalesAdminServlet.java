package controller;

import java.io.IOException;

import service.SalesService;
import service.impl.SalesServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import util.AppConstants;

/**
 * 管理者用の売上管理画面を制御するサーブレットです。
 */
@WebServlet("/Admin/Sales")
public class SalesAdminServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final SalesService salesService;

    public SalesAdminServlet() {
        this(new SalesServiceImpl());
    }

    public SalesAdminServlet(SalesService salesService) {
        this.salesService = salesService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(AppConstants.ATTR_TOTAL_SALES, salesService.getTotalSales());
        request.setAttribute(AppConstants.ATTR_DAILY_SALES, salesService.findDailySales());
        request.setAttribute(AppConstants.ATTR_PRODUCT_RANKING, salesService.findProductSalesRanking());

        request.getRequestDispatcher(AppConstants.VIEW_ADMIN_SALES).forward(request, response);
    }
}
