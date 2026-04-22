package service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import database.OrderDAO;
import database.TransactionManager;
import database.impl.OrderDAOImpl;
import model.CartItem;
import model.OrderConstants;
import service.OrderService;

/**
 * 注文業務のビジネスロジックを管理するService実装クラスです。
 */
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());
    private final OrderDAO orderDAO;

    public OrderServiceImpl() {
        this.orderDAO = new OrderDAOImpl();
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

                logger.info("注文登録完了: tableId=" + tableId + ", orderId=" + orderId);
                return true;
            });
        } catch (Exception e) {
            logger.severe("注文登録失敗: tableId=" + tableId + ", reason=" + e.getMessage());
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

                logger.info("会計完了処理成功: tableId=" + tableId);
                return true;
            });
        } catch (Exception e) {
            logger.severe("会計完了処理失敗: tableId=" + tableId + ", reason=" + e.getMessage());
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

    @Override
    public List<model.TableOrderSummary> findUnsettledTables() {
        return orderDAO.findUnsettledTables();
    }

    @Override
    public model.TableOrderSummary getTableOrderSummary(int tableId) {
        return orderDAO.getTableOrderSummary(tableId);
    }

    @Override
    public int getTotalSales() {
        return orderDAO.getTotalSales();
    }

    @Override
    public List<model.DailySales> findDailySales() {
        return orderDAO.findDailySales();
    }

    @Override
    public List<model.ProductSales> findProductSalesRanking() {
        return orderDAO.findProductSalesRanking();
    }

    @Override
    public List<model.TableStatusView> findAllTableStatus() {
        return orderDAO.findAllTableStatus();
    }
}
