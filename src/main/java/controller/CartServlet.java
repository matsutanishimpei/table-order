package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CartItem;
import model.Product;

/**
 * カートの操作（追加・削除・数量変更）を行うサーブレットです。
 */
@WebServlet("/Cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        // カートをセッションから取得、なければ作成
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        if ("add".equals(action)) {
            int productId = util.ValidationUtil.parseIntSafe(request.getParameter("productId"), -1);
            if (productId > 0) {
                addItem(cart, productId);
            }
        } else if ("remove".equals(action)) {
            int productId = util.ValidationUtil.parseIntSafe(request.getParameter("productId"), -1);
            if (productId > 0) {
                removeItem(cart, productId);
            }
        } else if ("clear".equals(action)) {
            cart.clear();
        }

        // 元の画面（Menu）に戻る
        String categoryId = request.getParameter("categoryId");
        response.sendRedirect("Menu" + (categoryId != null ? "?categoryId=" + categoryId : ""));
    }

    private void addItem(List<CartItem> cart, int productId) {
        // すでにカートにあるか確認
        for (CartItem item : cart) {
            if (item.getProductId() == productId) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        // 新規追加
        ProductDAO dao = new ProductDAO();
        Product p = dao.findById(productId);
        if (p != null) {
            cart.add(new CartItem(p.getId(), p.getName(), p.getPrice(), 1));
        }
    }

    private void removeItem(List<CartItem> cart, int productId) {
        cart.removeIf(item -> item.getProductId() == productId);
    }
}
