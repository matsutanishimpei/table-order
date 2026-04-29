package database.impl;

import database.JdbcExecutor;
import database.RowMapper;
import database.SqlConstants;
import database.TableDAO;

import java.util.List;
import java.util.Optional;

import model.OrderConstants;
import model.OrderItemView;
import model.TableOrderSummary;
import model.TableStatusView;

/**
 * 座席（テーブル）状態に関するデータベース操作を行うDAO実装クラスです。
 */
public class TableDAOImpl implements TableDAO {

    private final RowMapper<TableOrderSummary> summaryMapper = rs -> new TableOrderSummary(
            rs.getInt("id"),
            rs.getString("table_name"),
            null, // findUnsettledTables では明細リストは取得しない
            rs.getInt("total_amt"),
            rs.getInt("item_count"),
            rs.getInt("unserved_count")
    );

    private final RowMapper<TableStatusView> statusMapper = rs -> {
        int itemCount = rs.getInt("item_count");
        Object minStatusObj = rs.getObject("min_status");
        int minStatus = (minStatusObj == null || itemCount == 0) ? 0 : (Integer) minStatusObj;

        return new TableStatusView(
                rs.getInt("id"),
                rs.getString("table_name"),
                null, // statusLabel
                String.valueOf(minStatus),
                itemCount,
                rs.getInt("total_amt"),
                rs.getTimestamp("last_order")
        );
    };

    @Override
    public List<TableOrderSummary> findUnsettledTables() {
        return JdbcExecutor.query(SqlConstants.TABLE_SELECT_UNSETTLED, summaryMapper,
                OrderConstants.STATUS_SERVED, OrderConstants.STATUS_PAID);
    }

    @Override
    public Optional<TableOrderSummary> getTableOrderSummary(int tableId) {
        String sqlTable = SqlConstants.TABLE_SELECT_NAME_BY_ID;
        String sqlItems = SqlConstants.TABLE_SELECT_ITEMS_BY_TABLE_ID;

        String tableName = JdbcExecutor.queryOne(sqlTable, rs -> rs.getString("table_name"), tableId)
                .orElse(null);
        if (tableName == null) {
            return Optional.empty();
        }

        List<OrderItemView> items = JdbcExecutor.query(sqlItems, rs -> new OrderItemView(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("quantity"),
                null,
                null,
                rs.getInt("status"),
                rs.getInt("unit_price")
        ), tableId, OrderConstants.STATUS_PAID);

        int total = items.stream().mapToInt(i -> i.quantity() * i.unitPrice()).sum();
        int unserved = (int) items.stream().filter(i -> i.status() < OrderConstants.STATUS_SERVED).count();

        return Optional.of(new TableOrderSummary(tableId, tableName, items, total, items.size(), unserved));
    }

    @Override
    public List<TableStatusView> findAllTableStatus() {
        return JdbcExecutor.query(SqlConstants.TABLE_SELECT_ALL_STATUS, statusMapper,
                OrderConstants.STATUS_PAID, OrderConstants.STATUS_PAID);
    }
}
