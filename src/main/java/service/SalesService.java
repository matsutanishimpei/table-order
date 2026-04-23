package service;

import java.util.List;
import model.DailySales;
import model.ProductSales;

/**
 * 売上集計に関するビジネスロジックを定義するインターフェースです。
 */
public interface SalesService {
    /**
     * 全期間の累計売上額を取得します。
     * @return 累計売上
     */
    int getTotalSales();

    /**
     * 日次売上集計を取得します。
     * @return 日次売上リスト
     */
    List<DailySales> findDailySales();

    /**
     * 商品別の売上ランキングを取得します。
     * @return 商品別売上リスト
     */
    List<ProductSales> findProductSalesRanking();
}
