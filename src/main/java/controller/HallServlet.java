package controller;

import java.io.IOException;
import java.util.List;

import database.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.OrderConstants;
import model.OrderItemView;
import model.User;

/**
 * ホール用の配膳管理サーブレットです。
 */
@WebServlet("/Hall/Home")
public class HallServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 配膳待ち（調理完了）商品の取得
        OrderDAO dao = new OrderDAO();
        List<OrderItemView> readyItems = dao.findReadyOrderItems();
        request.setAttribute("readyItems", readyItems);

        // 配膳画面へ
        request.getRequestDispatcher("/WEB-INF/view/hall.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        OrderDAO dao = new OrderDAO();

        if ("serve".equals(action)) {
            // 配膳完了処理
            int itemId = util.ValidationUtil.parseIntSafe(request.getParameter("itemId"), -1);
            if (itemId > 0) {
                dao.updateItemStatus(itemId, OrderConstants.STATUS_SERVED);
            }
        }

        // 画面をリロード
        response.sendRedirect("Home");
    }
}
