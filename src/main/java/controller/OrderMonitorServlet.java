package controller;

import java.io.IOException;
import java.util.List;

import database.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TableStatusView;

/**
 * 店舗全体の受注状況を監視するダッシュボードを表示するサーブレットです。
 */
@WebServlet("/Admin/OrderMonitor")
public class OrderMonitorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDAO dao = new OrderDAO();
        
        // 全テーブルのステータス一覧を取得
        List<TableStatusView> tableList = dao.findAllTableStatus();

        // リクエストスコープにセット
        request.setAttribute("tableList", tableList);

        // JSPへフォワード
        request.getRequestDispatcher("/WEB-INF/view/admin_order_monitor.jsp").forward(request, response);
    }
}
