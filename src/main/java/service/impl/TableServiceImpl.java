package service.impl;

import java.util.List;
import java.util.Optional;

import database.TableDAO;
import database.impl.TableDAOImpl;
import model.OrderConstants;
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
    public Optional<TableOrderSummary> getTableOrderSummary(int tableId) {
        return tableDAO.getTableOrderSummary(tableId);
    }

    @Override
    public List<TableStatusView> findAllTableStatus() {
        List<TableStatusView> list = tableDAO.findAllTableStatus();
        // Record は不変なため、Stream API を使用して新しいラベルをセットした Record を生成して返す
        return list.stream()
            .map(view -> new TableStatusView(
                view.tableId(),
                view.tableName(),
                resolveStatusLabel(view.statusCode()),
                view.statusCode(),
                view.orderCount(),
                view.totalAmount(),
                view.lastOrderTime()
            ))
            .toList();
    }

    /**
     * ステータスコードから表示用ラベルを返します。
     */
    private String resolveStatusLabel(String statusCode) {
        if (statusCode == null) {
            return "空席";
        }
        int code;
        try {
            code = Integer.parseInt(statusCode);
        } catch (NumberFormatException e) {
            return "空席";
        }

        if (code == OrderConstants.STATUS_ORDERED) {
            return "調理待ち";
        } else if (code == OrderConstants.STATUS_COOKING_DONE) {
            return "配膳待ち";
        } else if (code == OrderConstants.STATUS_SERVED) {
            return "食事中";
        }
        return "空席";
    }
}
