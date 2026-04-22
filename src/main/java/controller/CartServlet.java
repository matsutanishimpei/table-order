package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.ProductDAO;
import database.impl.ProductDAOImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CartItem;
import model.Product;

/**
 * カート操作（追加・削除・表示）を制御するサーブレットです。
 */
@WebServlet("/Cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductDAO productDAO = new ProductDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/menu.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                boolean found = false;
                for (CartItem item : cart) {
                    if (item.getProductId() == productId) {
                        item.setQuantity(item.getQuantity() + quantity);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    ProductDAO dao = productDAO;
                    Product p = dao.findById(productId);
                    if (p != null) {
                        CartItem newItem = new CartItem();
                        newItem.setProductId(p.getId());
                        newItem.setName(p.getName()); // setName に修正
                        newItem.setUnitPrice(p.getPrice());
                        newItem.setQuantity(quantity);
                        cart.add(newItem);
                    }
                }
            } catch (NumberFormatException e) {
                // 不正な値が送られた場合は何もしない（またはエラーログ）
            }
        } else if ("clear".equals(action)) {
            cart.clear();
        }

        // メニュー画面に戻る（categoryIdが取得できない場合はデフォルト値を想定）
        String categoryId = request.getParameter("categoryId");
        if (categoryId == null || categoryId.isEmpty()) {
            response.sendRedirect("Menu");
        } else {
            response.sendRedirect("Menu?categoryId=" + categoryId);
        }
    }
}
