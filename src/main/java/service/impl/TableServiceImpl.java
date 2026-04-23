package service.impl;

import java.util.List;

import database.TableDAO;
import database.impl.TableDAOImpl;
import model.TableOrderSummary;
import model.TableStatusView;
import service.TableService;

/**
 * 座席状態業務のビジネスロジックを管理するService実装クラスです。
 */
public class TableServiceImpl implements TableService {
    private final TableDAO tableDAO;

    // プロダクション用コンストラクタ
    public TableServiceImpl() {
        this(new TableDAOImpl());
    }

    // テスト・DI用コンストラクタ
    public TableServiceImpl(TableDAO tableDAO) {
        this.tableDAO = tableDAO;
    }

    @Override
    public List<TableOrderSummary> findUnsettledTables() {
        return tableDAO.findUnsettledTables();
    }

    @Override
    public TableOrderSummary getTableOrderSummary(int tableId) {
        return tableDAO.getTableOrderSummary(tableId);
    }

    @Override
    public List<TableStatusView> findAllTableStatus() {
        return tableDAO.findAllTableStatus();
    }
}
