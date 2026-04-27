package service.impl;

import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import database.OrderDAO;
import database.TransactionManager;
import database.impl.OrderDAOImpl;
import model.CartItem;
import model.OrderConstants;
import service.OrderService;

/**
 * 注文業務のビジネスロジックを管理するService実装クラスです。
 */
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderDAO orderDAO;

    // プロダクション用コンストラクタ
    public OrderServiceImpl() {
        this(new OrderDAOImpl());
    }

    // テスト・DI用コンストラクタ
    public OrderServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public boolean createOrder(int tableId, List<CartItem> cartItems) {
        try {
            return TransactionManager.execute(con -> {
                // ① orders テーブルへ登録し、生成された order_id を取得
                int orderId = orderDAO.insertOrder(con, tableId, OrderConstants.STATUS_ORDERED);
                if (orderId == -1) {
                    throw new SQLException("OrderID generation failed.");
                }

                // ② order_items テーブルへ全商品をバッチ登録
                orderDAO.insertOrderItems(con, orderId, cartItems, OrderConstants.STATUS_ORDERED);

                log.info("注文登録完了: tableId={}, orderId={}", tableId, orderId);
                return true;
            });
        } catch (Exception e) {
            log.error("注文登録失敗: tableId={}, reason={}", tableId, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean completeCheckout(int tableId) {
        try {
            return TransactionManager.execute(con -> {
                // ① まず order_items を PAID に変更
                orderDAO.updateOrderItemsStatusForCheckout(con, tableId, OrderConstants.STATUS_PAID, OrderConstants.STATUS_PAID);

                // ② 続いて orders 自身を PAID に変更
                orderDAO.updateOrderStatusForCheckout(con, tableId, OrderConstants.STATUS_PAID, OrderConstants.STATUS_PAID);

                log.info("会計完了処理成功: tableId={}", tableId);
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
    public boolean updateItemStatus(int itemId, int status) {
        return orderDAO.updateItemStatus(itemId, status);
    }


}
