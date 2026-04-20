package controller;

import java.io.IOException;
import java.util.List;

import database.OrderDAO;
import database.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.OrderConstants;
import model.OrderItemView;
import model.Product;
import model.User;

/**
 * キッチン用の管理サーブレットです。
 */
@WebServlet("/Kitchen/Home")
public class KitchenServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 調理待ち商品の取得
        OrderDAO oDao = new OrderDAO();
        List<OrderItemView> activeItems = oDao.findActiveOrderItems();
        request.setAttribute("activeItems", activeItems);

        // 全商品の取得（品切れ管理用）
        ProductDAO pDao = new ProductDAO();
        List<Product> allProducts = pDao.findAll();
        request.setAttribute("allProducts", allProducts);

        // キッチン画面へ
        request.getRequestDispatcher("/WEB-INF/view/kitchen.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        OrderDAO oDao = new OrderDAO();
        ProductDAO pDao = new ProductDAO();

        if ("complete".equals(action)) {
            // 調理完了処理
            int itemId = util.ValidationUtil.parseIntSafe(request.getParameter("itemId"), -1);
            if (itemId > 0) {
                oDao.updateItemStatus(itemId, OrderConstants.STATUS_COOKING_DONE);
            }
        } else if ("toggle_availability".equals(action)) {
            // 品切れ切り替え
            int productId = util.ValidationUtil.parseIntSafe(request.getParameter("productId"), -1);
            if (productId > 0) {
                boolean currentAvailable = Boolean.parseBoolean(request.getParameter("currentAvailable"));
                pDao.updateAvailability(productId, !currentAvailable);
            }
        }

        // 画面をリロード
        response.sendRedirect("Home");
    }
}
