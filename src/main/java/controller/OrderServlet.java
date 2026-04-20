package controller;

import java.io.IOException;
import java.util.List;

import database.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CartItem;
import model.User;

/**
 * 注文の確定処理を行うサーブレットです。
 */
@WebServlet("/Order")
public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (user == null || cart == null || cart.isEmpty()) {
            response.sendRedirect("Menu");
            return;
        }

        // DBに保存
        Integer tableId = user.getTableId();
        if (tableId == null) {
            request.setAttribute("error", "このアカウントには座席（テーブル）が割り当てられていないため、注文できません。");
            request.getRequestDispatcher("/WEB-INF/view/menu.jsp").forward(request, response);
            return;
        }

        OrderDAO dao = new OrderDAO();
        boolean success = dao.createOrder(tableId, cart);

        if (success) {
            // カートを空にする
            cart.clear();
            // 完了画面（または完了メッセージ付きのメニュー）へ
            request.setAttribute("message", "注文が確定しました！お届けまでしばらくお待ちください。");
            request.getRequestDispatcher("/WEB-INF/view/menu.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "注文処理中にエラーが発生しました。");
            request.getRequestDispatcher("/WEB-INF/view/menu.jsp").forward(request, response);
        }
    }
}
