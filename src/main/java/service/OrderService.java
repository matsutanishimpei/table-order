package service;

import java.util.List;
import model.CartItem;
import model.OrderItemView;

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

    /**
     * キッチン用のアクティブな注文明細を取得します。
     * @return 注文明細リスト
     */
    List<OrderItemView> findActiveOrderItems();

    /**
     * 配膳待ちの明細を取得します。
     * @return 注文明細リスト
     */
    List<OrderItemView> findReadyOrderItems();

    /**
     * 注文明細のステータスを更新します。
     * @param itemId 注文明細ID
     * @param status ステータス
     * @return 更新成功時は true
     */
    boolean updateItemStatus(int itemId, int status);


}
