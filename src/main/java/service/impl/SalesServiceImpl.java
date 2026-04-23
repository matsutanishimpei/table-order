package service.impl;

import java.util.List;

import database.SalesDAO;
import database.impl.SalesDAOImpl;
import model.DailySales;
import model.ProductSales;
import service.SalesService;

/**
 * 売上集計業務のビジネスロジックを管理するService実装クラスです。
 */
public class SalesServiceImpl implements SalesService {
    private final SalesDAO salesDAO;

    // プロダクション用コンストラクタ
    public SalesServiceImpl() {
        this(new SalesDAOImpl());
    }

    // テスト・DI用コンストラクタ
    public SalesServiceImpl(SalesDAO salesDAO) {
        this.salesDAO = salesDAO;
    }

    @Override
    public int getTotalSales() {
        return salesDAO.getTotalSales();
    }

    @Override
    public List<DailySales> findDailySales() {
        return salesDAO.findDailySales();
    }

    @Override
    public List<ProductSales> findProductSalesRanking() {
        return salesDAO.findProductSalesRanking();
    }
}
