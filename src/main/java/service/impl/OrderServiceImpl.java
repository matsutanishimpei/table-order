package service.impl;

import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import database.OrderDAO;
import database.TransactionManager;
import database.impl.OrderDAOImpl;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import model.CartItem;
import model.OrderConstants;
import service.OrderService;

/**
 * 注文業務のビジネスロジックを管理するService実装クラスです。
 */
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderDAO orderDAO;
    private final database.ProductDAO productDAO;

    // プロダクション用コンストラクタ
    public OrderServiceImpl() {
        this(new OrderDAOImpl(), new database.impl.ProductDAOImpl());
    }

    // テスト・DI用コンストラクタ
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public OrderServiceImpl(OrderDAO orderDAO, database.ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
    }

    @Override
    public boolean createOrder(int tableId, List<CartItem> cartItems, String operatorId) {
        try {
            return TransactionManager.execute(con -> {
                // ① 商品の販売状態（is_available）をチェック
                for (CartItem item : cartItems) {
                    java.util.Optional<model.Product> p = productDAO.findById(item.productId());
                    if (p.isEmpty() || !p.get().isAvailable()) {
                        log.warn("注文を中断しました（商品が利用不可能）: productId={}, name={}", 
                                item.productId(), item.name());
                        return false;
                    }
                }

                // ② アクティブな注文（セッション）があるか確認。なければ作成。
                // (SqlConstants.ORDER_SELECT_ACTIVE_ID には FOR UPDATE が含まれているため、ここでロックがかかる)
                int orderId = orderDAO.findActiveOrderIdByTable(con, tableId);
                if (orderId == -1) {
                    orderId = orderDAO.insertOrder(con, tableId, OrderConstants.STATUS_ORDERED, operatorId);
                    if (orderId == -1) {
                        throw new SQLException("OrderID generation failed.");
                    }
                    log.info("新規注文セッション作成: tableId={}, orderId={}, operatorId={}", tableId, orderId, operatorId);
                } else {
                    log.info("既存注文セッション再利用: tableId={}, orderId={}", tableId, orderId);
                }

                // ③ order_items テーブルへ全商品をバッチ登録
                orderDAO.insertOrderItems(con, orderId, cartItems, OrderConstants.STATUS_ORDERED, operatorId);

                log.info("注文登録完了: tableId={}, orderId={}", tableId, orderId);
                return true;
            });
        } catch (Exception e) {
            log.error("注文登録失敗: tableId={}, reason={}", tableId, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean completeCheckout(int tableId, String operatorId) {
        try {
            return TransactionManager.execute(con -> {
                // ① 未提供の商品（status < STATUS_SERVED）がないかチェック
                int unservedCount = orderDAO.countUnservedItemsByTable(con, tableId);
                if (unservedCount > 0) {
                    log.warn("会計完了処理を中断しました（未提供商品あり）: tableId={}, count={}", tableId, unservedCount);
                    return false;
                }

                // ② まず order_items を PAID に変更
                orderDAO.updateOrderItemsStatusForCheckout(con, tableId,
                        OrderConstants.STATUS_PAID, OrderConstants.STATUS_PAID, operatorId);

                // ③ 続いて orders 自身を PAID に変更
                orderDAO.updateOrderStatusForCheckout(con, tableId,
                        OrderConstants.STATUS_PAID, OrderConstants.STATUS_PAID, operatorId);

                log.info("会計完了処理成功: tableId={}, operatorId={}", tableId, operatorId);
                return true;
            });
        } catch (Exception e) {
            log.error("会計完了処理失敗: tableId={}, reason={}", tableId, e.getMessage());
            return false;
        }
    }

    @Override
    public List<model.OrderItemView> findActiveOrderItems() {
        return orderDAO.findActiveOrderItems();
    }

    @Override
    public List<model.OrderItemView> findReadyOrderItems() {
        return orderDAO.findReadyOrderItems();
    }

    @Override
    public boolean updateItemStatus(int itemId, int status, String operatorId) {
        int currentStatus = orderDAO.findItemStatusById(itemId);
        if (currentStatus == -1) {
            log.warn("ステータス更新失敗: 指定された明細が存在しません。itemId={}", itemId);
            return false;
        }

        // ステータスの逆行を防止（例: 配膳済み(30) -> 調理中(10) は不可）
        if (status < currentStatus) {
            log.warn("ステータス更新拒否: 過去の状態への変更は許可されていません。itemId={}, current={}, target={}",
                    itemId, currentStatus, status);
            return false;
        }

        return orderDAO.updateItemStatus(itemId, status, operatorId);
    }
}
