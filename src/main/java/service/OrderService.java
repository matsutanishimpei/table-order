package service;

import java.util.List;
import model.CartItem;

/**
 * 注文業務のビジネスロジックを定義するインターフェースです。
 */
public interface OrderService {
    /**
     * 注文を登録します。
     * 
     * @param tableId 座席ID
     * @param cartItems カート内の商品リスト
     * @return 成功時は true
     */
    boolean createOrder(int tableId, List<CartItem> cartItems);

    /**
     * 特定の座席の会計を完了させます。
     * 
     * @param tableId 座席ID
     * @return 成功時は true
     */
    boolean completeCheckout(int tableId);
}
