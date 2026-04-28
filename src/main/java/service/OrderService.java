package service;

import java.util.List;
import model.CartItem;
import model.OrderItemView;

/**
 * 注文業務のビジネスロジックを定義するインターフェースです。
 * 注文の作成、提供状況の管理、および会計処理を提供します。
 */
public interface OrderService {
    /**
     * 新規注文を登録します。
     * カート内の商品を一括で注文データとして保存し、座席の状態を更新します。
     *
     * @param tableId 注文元の座席（テーブル）ID
     * @param cartItems 注文する商品のリスト（不変な CartItem レコードのリスト）
     * @return 登録に成功した場合は true
     * @throws exception.BusinessException 商品が販売不可の場合や、座席が不正な場合に発生します
     */
    boolean createOrder(int tableId, List<CartItem> cartItems);

    /**
     * 特定の座席の会計（チェックアウト）を完了させます。
     * すべての商品が提供済みであることを確認し、座席を空席状態に戻します。
     *
     * @param tableId 会計対象の座席ID
     * @return 会計処理に成功した場合は true
     * @throws exception.BusinessException 未提供の商品が残っている場合などに発生します
     */
    boolean completeCheckout(int tableId);

    /**
     * 厨房（キッチン）向けに、現在調理が必要な、または調理中の注文明細をすべて取得します。
     *
     * @return 注文明細のリスト（表示用ビューモデル）
     */
    List<OrderItemView> findActiveOrderItems();

    /**
     * ホール（配膳担当）向けに、調理が完了し配膳を待っている注文明細をすべて取得します。
     *
     * @return 注文明細のリスト（表示用ビューモデル）
     */
    List<OrderItemView> findReadyOrderItems();

    /**
     * 注文明細のステータス（調理中、調理完了、提供済みなど）を更新します。
     *
     * @param itemId 注文明細ID
     * @param status 更新後のステータス（OrderConstants に定義された定数）
     * @return 更新に成功した場合は true
     */
    boolean updateItemStatus(int itemId, int status);
}
