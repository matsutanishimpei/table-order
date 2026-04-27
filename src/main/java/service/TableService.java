package service;

import java.util.List;
import java.util.Optional;
import model.TableOrderSummary;
import model.TableStatusView;

/**
 * 座席（テーブル）状態に関するビジネスロジックを定義するインターフェースです。
 */
public interface TableService {
    /**
     * 未会計の座席一覧を取得します。
     * @return 座席注文サマリーのリスト
     */
    List<TableOrderSummary> findUnsettledTables();

    /**
     * 特定の座席の注文サマリーを取得します。
     * @param tableId 座席ID
     * @return 座席注文サマリーを含むOptional
     */
    Optional<TableOrderSummary> getTableOrderSummary(int tableId);

    /**
     * 全テーブルの現在のステータスを取得します。
     * @return テーブルステータスリスト
     */
    List<TableStatusView> findAllTableStatus();
}
