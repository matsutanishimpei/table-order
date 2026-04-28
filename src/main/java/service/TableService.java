package service;

import java.util.List;
import java.util.Optional;
import model.TableOrderSummary;
import model.TableStatusView;

/**
 * 座席（テーブル）状態に関するビジネスロジックを定義するインターフェースです。
 * 座席の稼働状況の把握や、会計前の注文集計情報の提供を行います。
 */
public interface TableService {
    /**
     * 未精算（注文があり、会計が終わっていない）の座席一覧を、注文合計金額とともに取得します。
     * 主にレジ画面（CashierServlet）で使用されます。
     *
     * @return 座席ごとの注文サマリーのリスト。未精算の座席がない場合は空のリストを返します。
     */
    List<TableOrderSummary> findUnsettledTables();

    /**
     * 指定された座席の、未精算の注文明細を含むサマリーを取得します。
     *
     * @param tableId 座席ID
     * @return 座席注文サマリーを含む Optional インスタンス。該当する注文がない場合は空の Optional を返します。
     */
    Optional<TableOrderSummary> getTableOrderSummary(int tableId);

    /**
     * システム内の全座席の現在の状態（空席、利用中、未提供あり等）を一覧で取得します。
     * モニター画面や座席選択画面で使用されます。
     *
     * @return テーブルステータス（表示用ビュー）のリスト
     */
    List<TableStatusView> findAllTableStatus();
}
