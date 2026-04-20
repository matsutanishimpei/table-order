package controller;

import java.io.IOException;
import java.util.List;

import database.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.DailySales;
import model.ProductSales;

/**
 * 売上管理画面を表示するサーブレットです。
 */
@WebServlet("/Admin/Sales")
public class SalesAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDAO dao = new OrderDAO();

        // 1. 累計売上高の取得
        int totalSales = dao.getTotalSales();
        
        // 2. 直近7日間の売上トレンド取得
        List<DailySales> dailySales = dao.findDailySales();
        
        // 3. 商品別の売上ランキング取得
        List<ProductSales> productRanking = dao.findProductSalesRanking();

        // リクエストスコープにセット
        request.setAttribute("totalSales", totalSales);
        request.setAttribute("dailySales", dailySales);
        request.setAttribute("productRanking", productRanking);

        // JSPへフォワード
        request.getRequestDispatcher("/WEB-INF/view/admin_sales.jsp").forward(request, response);
    }
}
