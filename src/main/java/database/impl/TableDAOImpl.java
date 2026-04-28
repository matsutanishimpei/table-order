package database.impl;

import database.DBManager;
import database.SqlConstants;
import database.TableDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    @Override
    public List<TableOrderSummary> findUnsettledTables() {
        List<TableOrderSummary> list = new ArrayList<>();
        String sql = SqlConstants.TABLE_SELECT_UNSETTLED;

        try (Connection con = DBManager.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, OrderConstants.STATUS_SERVED);
            ps.setInt(2, OrderConstants.STATUS_PAID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new TableOrderSummary(
                            rs.getInt("id"),
                            rs.getString("table_name"),
                            null, // findUnsettledTables では明細リストは取得しない
                            rs.getInt("total_amt"),
                            rs.getInt("item_count"),
                            rs.getInt("unserved_count")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("未精算テーブル一覧の取得中にエラーが発生しました。", e);
        }
        return list;
    }

    @Override
    public Optional<TableOrderSummary> getTableOrderSummary(int tableId) {
        String sqlTable = SqlConstants.TABLE_SELECT_NAME_BY_ID;
        String sqlItems = SqlConstants.TABLE_SELECT_ITEMS_BY_TABLE_ID;

        try (Connection con = DBManager.getConnection()) {
            String tableName = null;
            try (PreparedStatement psTable = con.prepareStatement(sqlTable)) {
                psTable.setInt(1, tableId);
                try (ResultSet rsTable = psTable.executeQuery()) {
                    if (rsTable.next()) {
                        tableName = rsTable.getString("table_name");
                    } else {
                        return Optional.empty(); // テーブルが存在しない
                    }
                }
            }

            List<OrderItemView> items = new ArrayList<>();
            int total = 0;
            try (PreparedStatement psItems = con.prepareStatement(sqlItems)) {
                psItems.setInt(1, tableId);
                psItems.setInt(2, OrderConstants.STATUS_PAID);
                try (ResultSet rsItems = psItems.executeQuery()) {
                    while (rsItems.next()) {
                        OrderItemView item = new OrderItemView(
                                rsItems.getInt("id"),
                                rsItems.getString("name"),
                                rsItems.getInt("quantity"),
                                null, // tableName はこのクエリでは取得していない
                                null, // orderedAt はこのクエリでは取得していない
                                rsItems.getInt("status"),
                                rsItems.getInt("unit_price")
                        );
                        items.add(item);
                        total += item.quantity() * item.unitPrice();
                    }
                }
            }
            return Optional.of(new TableOrderSummary(
                tableId,
                tableName,
                items,
                total,
                items.size(),
                (int) items.stream().filter(i -> i.status() < OrderConstants.STATUS_SERVED).count()
            ));
        } catch (SQLException e) {
            throw new exception.DatabaseException("座席別注文サマリーの取得中にエラーが発生しました。tableId=" + tableId, e);
        }
    }

    @Override
    public List<TableStatusView> findAllTableStatus() {
        List<TableStatusView> list = new ArrayList<>();
        String sql = SqlConstants.TABLE_SELECT_ALL_STATUS;

        try (Connection con = DBManager.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, OrderConstants.STATUS_PAID);
            ps.setInt(2, OrderConstants.STATUS_PAID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int itemCount = rs.getInt("item_count");
                    Object minStatusObj = rs.getObject("min_status");
                    int minStatus = (minStatusObj == null || itemCount == 0) ? 0 : (Integer) minStatusObj;

                    TableStatusView view = new TableStatusView(
                            rs.getInt("id"),
                            rs.getString("table_name"),
                            null, // statusLabel は Service 等で設定される想定か？
                            String.valueOf(minStatus),
                            itemCount,
                            rs.getInt("total_amt"),
                            rs.getTimestamp("last_order")
                    );
                    list.add(view);
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("全テーブルのステータス取得中にエラーが発生しました。", e);
        }
        return list;
    }
}
