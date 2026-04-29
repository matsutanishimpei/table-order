package controller;

import java.io.IOException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import service.SalesService;
import service.ServiceFactory;

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

    @SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final SalesService salesService;

    public SalesAdminServlet() {
        this(ServiceFactory.getSalesService());
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public SalesAdminServlet(SalesService salesService) {
        this.salesService = salesService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 権限チェック：管理者権限がない場合は 403 エラー
        model.User user = (model.User) request.getSession().getAttribute(AppConstants.ATTR_USER);
        if (user == null || !user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute(AppConstants.ATTR_TOTAL_SALES, salesService.getTotalSales());
        request.setAttribute(AppConstants.ATTR_DAILY_SALES, salesService.findDailySales());
        request.setAttribute(AppConstants.ATTR_PRODUCT_RANKING, salesService.findProductSalesRanking());

        request.getRequestDispatcher(AppConstants.VIEW_ADMIN_SALES).forward(request, response);
    }
}
