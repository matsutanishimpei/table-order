package database;

import java.util.List;
import model.DailySales;
import model.ProductSales;

/**
 * 売上集計に関するデータベース操作を行うDAOインターフェースです。
 * 注文データから累計売上、日別推移、ランキング情報の抽出を担当します。
 */
public interface SalesDAO {
    /**
     * 支払い済みとなっている全期間の累計売上金額を取得します。
     * @return 累計売上合計金額（円）
     */
    int getTotalSales();

    /**
     * 過去の日次売上実績を日付ごとに集計して取得します。
     * @return 日次売上（DailySales レコード）のリスト。日付の昇順で返されます。
     */
    List<DailySales> findDailySales();

    /**
     * 商品ごとの累計売上実績を集計し、売上金額の高い順に取得します。
     * @return 商品別売上（ProductSales レコード）のリスト。売上合計額の降順で返されます。
     */
    List<ProductSales> findProductSalesRanking();
}
