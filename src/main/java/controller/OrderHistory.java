package controller;

import java.io.IOException;
import database.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.TableOrderSummary;
import model.User;

/**
 * 現在の座席の注文履歴を表示するサーブレットです。
 */
@WebServlet("/OrderHistory")
public class OrderHistory extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // セッション確認
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("Login");
            return;
        }

        User user = (User) session.getAttribute("user");
        Integer tableId = user.getTableId();

        if (tableId == null) {
            response.sendRedirect("Menu");
            return;
        }

        // 注文履歴サマリーを取得
        OrderDAO oDao = new OrderDAO();
        TableOrderSummary history = oDao.getTableOrderSummary(tableId);

        request.setAttribute("history", history);

        // 注文履歴画面へフォワード
        request.getRequestDispatcher("/WEB-INF/view/order_history.jsp").forward(request, response);
    }
}
