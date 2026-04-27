package controller;

import java.io.IOException;
import java.util.Optional;

import service.TableService;
import service.impl.TableServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.TableOrderSummary;
import model.User;

/**
 * 座席ごとの注文履歴を表示するサーブレットです。
 */
@WebServlet("/OrderHistory")
public class OrderHistoryServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final TableService tableService;

    public OrderHistoryServlet() {
        this(new TableServiceImpl());
    }

    public OrderHistoryServlet(TableService tableService) {
        this.tableService = tableService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getTableId() == null) {
            response.sendRedirect("Login");
            return;
        }

        Optional<TableOrderSummary> summaryOpt = tableService.getTableOrderSummary(user.getTableId());
        
        request.setAttribute("summary", summaryOpt.orElse(null));
        request.getRequestDispatcher("/WEB-INF/view/order_history.jsp").forward(request, response);
    }
}
