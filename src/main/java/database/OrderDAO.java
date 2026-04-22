package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.CartItem;
import model.OrderItemView;
import model.TableOrderSummary;

/**
 * 注文情報のデータベース操作を行うDAOインターフェースです。
 */
public interface OrderDAO {
    /**
     * 新規注文を登録し、生成された order_id を返します。
     * @param con データベース接続
     * @param tableId 座席ID
     * @param status ステータス
     * @return 生成された order_id
     * @throws SQLException データベースエラー
     */
    int insertOrder(Connection con, int tableId, int status) throws SQLException;

    /**
     * 注文詳細を複数登録します。
     * @param con データベース接続
     * @param orderId 注文ID
     * @param cartItems カート内容
     * @param status ステータス
     * @throws SQLException データベースエラー
     */
    void insertOrderItems(Connection con, int orderId, List<CartItem> cartItems, int status) throws SQLException;

    /**
     * キッチン用のアクティブな注文明細を取得します。
     * @return 注文明細リスト
     */
    List<OrderItemView> findActiveOrderItems();

    /**
     * 注文明細のステータスを更新します。
     * @param itemId 注文明細ID
     * @param status ステータス
     * @return 更新成功時は true
     */
    boolean updateItemStatus(int itemId, int status);

    /**
     * 配膳待ちの明細を取得します。
     * @return 注文明細リスト
     */
    List<OrderItemView> findReadyOrderItems();

    /**
     * 未会計の座席一覧を取得します。
     * @return 座席注文サマリーのリスト
     */
    List<TableOrderSummary> findUnsettledTables();

    /**
     * 特定の座席の注文サマリーを取得します。
     * @param tableId 座席ID
     * @return 座席注文サマリー
     */
    TableOrderSummary getTableOrderSummary(int tableId);

    /**
     * 会計時の注文明細ステータス更新を行います。
     * @param con データベース接続
     * @param tableId 座席ID
     * @param targetStatus 更新後のステータス
     * @param conditionStatusLt 条件となるステータス（これ未満）
     * @throws SQLException データベースエラー
     */
    void updateOrderItemsStatusForCheckout(Connection con, int tableId, int targetStatus, int conditionStatusLt) throws SQLException;

    /**
     * 会計時の注文ステータス更新を行います。
     * @param con データベース接続
     * @param tableId 座席ID
     * @param targetStatus 更新後のステータス
     * @param conditionStatusLt 条件となるステータス（これ未満）
     * @throws SQLException データベースエラー
     */
    void updateOrderStatusForCheckout(Connection con, int tableId, int targetStatus, int conditionStatusLt) throws SQLException;

    /**
     * 全テーブルの現在のステータスを取得します。
     * @return テーブルステータスリスト
     */
    List<model.TableStatusView> findAllTableStatus();

    /**
     * 全期間の累計売上額を取得します。
     * @return 累計売上
     */
    int getTotalSales();

    /**
     * 日次売上集計を取得します。
     * @return 日次売上リスト
     */
    List<model.DailySales> findDailySales();

    /**
     * 商品別の売上ランキングを取得します。
     * @return 商品別売上リスト
     */
    List<model.ProductSales> findProductSalesRanking();
}
