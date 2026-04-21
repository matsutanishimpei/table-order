package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import database.DBManager;
import database.OrderDAO;
import model.CartItem;
import model.OrderConstants;

/**
 * 注文業務のビジネスロジックとトランザクション（DB境界）を管理するServiceクラスです。
 */
public class OrderService {
    private static final Logger logger = Logger.getLogger(OrderService.class.getName());
    private final OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    /**
     * 注文を登録します。ORDERSテーブルとORDER_ITEMSテーブルをトランザクション制御して保存します。
     * 
     * @param tableId 座席ID
     * @param cartItems カート内の商品リスト
     * @return 成功時は true
     */
    public boolean createOrder(int tableId, List<CartItem> cartItems) {
        Connection con = null;
        try {
            con = DBManager.getConnection();
            // トランザクション開始
            con.setAutoCommit(false);

            // ① orders テーブルへ登録し、生成された order_id を取得
            int orderId = orderDAO.insertOrder(con, tableId, OrderConstants.STATUS_ORDERED);
            if (orderId == -1) {
                throw new SQLException("OrderID generation failed.");
            }

            // ② order_items テーブルへ全商品をバッチ登録
            orderDAO.insertOrderItems(con, orderId, cartItems, OrderConstants.STATUS_ORDERED);

            // コミット
            con.commit();
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    // ロールバック処理
                    con.rollback();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "注文登録のロールバック中にエラーが発生しました。", ex);
                }
            }
            logger.log(Level.SEVERE, "注文登録処理（トランザクション）中にエラーが発生しました。tableId=" + tableId, e);
            return false;
        } finally {
            // プールへコネクションを返却
            DBManager.closeConnection(con);
        }
    }

    /**
     * 特定の座席の会計を完了させます（トランザクション制御下で関連するステータスを更新）。
     * 
     * @param tableId 座席ID
     * @return 成功時は true
     */
    public boolean completeCheckout(int tableId) {
        Connection con = null;
        try {
            con = DBManager.getConnection();
            con.setAutoCommit(false); // トランザクション開始

            // ① まず order_items を PAID に変更
            orderDAO.updateOrderItemsStatusForCheckout(con, tableId, OrderConstants.STATUS_PAID, OrderConstants.STATUS_PAID);

            // ② 続いて orders 自身を PAID に変更
            orderDAO.updateOrderStatusForCheckout(con, tableId, OrderConstants.STATUS_PAID, OrderConstants.STATUS_PAID);

            con.commit();
            return true;
            
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "会計完了のロールバック中にエラーが発生しました。", ex);
                }
            }
            logger.log(Level.SEVERE, "会計完了処理（トランザクション）中にエラーが発生しました。tableId=" + tableId, e);
            return false;
        } finally {
            DBManager.closeConnection(con);
        }
    }
}
