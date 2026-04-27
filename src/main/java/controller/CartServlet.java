package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import service.ProductService;
import service.impl.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CartItem;

import util.AppConstants;

/**
 * カート操作（追加・削除・表示）を制御するサーブレットです。
 */
@WebServlet("/Cart")
public class CartServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final ProductService productService;

    public CartServlet() {
        this(new ProductServiceImpl());
    }

    public CartServlet(ProductService productService) {
        this.productService = productService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(AppConstants.VIEW_MENU).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<CartItem> sessionCart = (List<CartItem>) session.getAttribute(AppConstants.ATTR_CART);
        if (sessionCart == null) {
            sessionCart = new ArrayList<>();
            session.setAttribute(AppConstants.ATTR_CART, sessionCart);
        }
        final List<CartItem> cart = sessionCart;

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            int productId = util.ValidationUtil.parseIntSafe(request.getParameter("productId"), -1);
            int quantity = util.ValidationUtil.parseIntSafe(request.getParameter("quantity"), 0);
            if (productId < 0 || quantity <= 0) {
                response.sendRedirect(AppConstants.REDIRECT_MENU);
                return;
            }

            Optional<CartItem> existingItem = cart.stream()
                .filter(item -> item.productId() == productId)
                .findFirst();

            if (existingItem.isPresent()) {
                CartItem oldItem = existingItem.get();
                int index = cart.indexOf(oldItem);
                // Record は不変なため、新しい数量で新しいインスタンスを作成して置換する
                CartItem newItem = new CartItem(
                    oldItem.productId(),
                    oldItem.name(),
                    oldItem.unitPrice(),
                    oldItem.quantity() + quantity
                );
                cart.set(index, newItem);
            } else {
                productService.findById(productId).ifPresent(p -> {
                    cart.add(new CartItem(p.id(), p.name(), p.price(), quantity));
                });
            }
        } else if ("clear".equals(action)) {
            cart.clear();
        }

        // メニュー画面に戻る（categoryIdが取得できない場合はデフォルト値を想定）
        String categoryId = request.getParameter("categoryId");
        if (categoryId == null || categoryId.isEmpty()) {
            response.sendRedirect(AppConstants.REDIRECT_MENU);
        } else {
            response.sendRedirect(AppConstants.REDIRECT_MENU + "?categoryId=" + categoryId);
        }
    }
}
