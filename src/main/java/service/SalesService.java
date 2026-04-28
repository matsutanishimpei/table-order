package service;

import java.util.List;
import model.DailySales;
import model.ProductSales;

/**
 * 売上集計に関するビジネスロジックを定義するインターフェースです。
 * 累計売上、日別推移、および商品別の売れ筋分析機能を提供します。
 */
public interface SalesService {
    /**
     * システム上の全期間の確定済み累計売上額を取得します。
     *
     * @return 累計売上金額（円）
     */
    int getTotalSales();

    /**
     * 日次の売上集計データを取得します。
     * 日付ごとの売上合計と注文件数を含みます。
     *
     * @return 日次売上（DailySales レコード）のリスト。時系列順で返されます。
     */
    List<DailySales> findDailySales();

    /**
     * 商品別の累計売上ランキングを取得します。
     * 販売個数と合計金額を商品ごとに集計します。
     *
     * @return 商品別売上（ProductSales レコード）のリスト。売上金額の降順でソートされます。
     */
    List<ProductSales> findProductSalesRanking();
}
