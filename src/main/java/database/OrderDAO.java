package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.CartItem;
import model.OrderItemView;

/**
 * 注文情報のデータベース操作を行うDAOインターフェースです。
 * 注文ヘッダー、注文明細の永続化、およびステータス管理を担います。
 */
public interface OrderDAO {

    /**
     * 新規注文（ヘッダー）を登録します。
     * トランザクション管理下で呼び出されることを想定しています。
     *
     * @param con データベース接続オブジェクト
     * @param tableId 注文元の座席（テーブル）ID
     * @param status 初期ステータス（OrderConstants に定義された定数）
     * @return データベースで自動生成された order_id
     * @throws SQLException データベースエラーが発生した場合
     */
    int insertOrder(Connection con, int tableId, int status, String operatorId) throws SQLException;

    /**
     * 注文明細を複数一括で登録します。
     * トランザクション管理下で呼び出されることを想定しています。
     *
     * @param con データベース接続オブジェクト
     * @param orderId 紐付ける親注文のID
     * @param cartItems 登録する商品リスト
     * @param status 初期ステータス
     * @throws SQLException データベースエラーが発生した場合
     */
    void insertOrderItems(Connection con, int orderId, List<CartItem> cartItems,
            int status, String operatorId) throws SQLException;

    /**
     * キッチン（厨房）向けに、調理が必要な（未提供の）注文明細をすべて取得します。
     *
     * @return 注文明細のリスト（OrderItemView ビューモデル）
     */
    List<OrderItemView> findActiveOrderItems();

    /**
     * 個別の注文明細のステータス（調理中、調理完了など）を更新します。
     *
     * @param itemId 注文明細ID
     * @param status 更新後のステータス
     * @param operatorId 操作者のユーザーID
     * @return 更新に成功した場合は true
     */
    boolean updateItemStatus(int itemId, int status, String operatorId);

    /**
     * ホール（配膳担当）向けに、調理が完了し配膳を待っている注文明細をすべて取得します。
     *
     * @return 注文明細のリスト（OrderItemView ビューモデル）
     */
    List<OrderItemView> findReadyOrderItems();

    /**
     * 会計処理の一環として、特定の座席の注文明細ステータスを一括更新します。
     * トランザクション管理下で呼び出されることを想定しています。
     *
     * @param con データベース接続オブジェクト
     * @param tableId 対象の座席ID
     * @param targetStatus 更新後のステータス（通常は「支払い済み」）
     * @param conditionStatusLt 条件となるステータス上限（これより小さいステータスのものを対象とする）
     * @param operatorId 操作者のユーザーID
     * @throws SQLException データベースエラーが発生した場合
     */
    void updateOrderItemsStatusForCheckout(Connection con, int tableId, int targetStatus,
            int conditionStatusLt, String operatorId) throws SQLException;

    /**
     * 会計処理の一環として、特定の座席の注文（ヘッダー）ステータスを一括更新します。
     * トランザクション管理下で呼び出されることを想定しています。
     *
     * @param con データベース接続オブジェクト
     * @param tableId 対象の座席ID
     * @param targetStatus 更新後のステータス（通常は「支払い済み」）
     * @param conditionStatusLt 条件となるステータス上限
     * @param operatorId 操作者のユーザーID
     * @throws SQLException データベースエラーが発生した場合
     */
    void updateOrderStatusForCheckout(Connection con, int tableId, int targetStatus,
            int conditionStatusLt, String operatorId) throws SQLException;

    /**
     * 特定の座席において、まだ配膳が完了していない（提供待ち）商品の件数を取得します。
     *
     * @param con データベース接続オブジェクト
     * @param tableId 対象の座席ID
     * @return 未提供商品の件数
     * @throws SQLException データベースエラーが発生した場合
     */
    int countUnservedItemsByTable(Connection con, int tableId) throws SQLException;

    /**
     * 指定された注文明細の現在のステータスを取得します。
     *
     * @param itemId 注文明細ID
     * @return 現在のステータス。存在しない場合は -1
     */
    int findItemStatusById(int itemId);

    /**
     * 指定された座席において、現在アクティブ（未会計）な注文IDを取得します。
     *
     * @param con データベース接続オブジェクト
     * @param tableId 座席ID
     * @return 注文ID。存在しない場合は -1
     * @throws SQLException データベースエラーが発生した場合
     */
    int findActiveOrderIdByTable(Connection con, int tableId) throws SQLException;
}
