package database;

import java.util.List;
import java.util.Optional;
import model.TableOrderSummary;
import model.TableStatusView;

/**
 * 座席（テーブル）状態に関するデータベース操作を行うDAOインターフェースです。
 * 各テーブルの利用状況、未精算の注文集計データの抽出を担当します。
 */
public interface TableDAO {
    /**
     * 未精算（支払い待ち）の注文が存在する座席の一覧を、注文合計金額とともに取得します。
     *
     * @return 座席ごとの注文サマリーのリスト。該当がない場合は空のリストを返します。
     */
    List<TableOrderSummary> findUnsettledTables();

    /**
     * 指定された座席の、現在の未精算注文明細を含むサマリー情報を取得します。
     *
     * @param tableId 対象の座席ID
     * @return 座席注文サマリーを含む Optional インスタンス
     */
    Optional<TableOrderSummary> getTableOrderSummary(int tableId);

    /**
     * システム内のすべての座席の最新ステータス（空席、調理中、提供済み等）を取得します。
     *
     * @return 座席ステータス（表示用ビュー）のリスト
     */
    List<TableStatusView> findAllTableStatus();
}
