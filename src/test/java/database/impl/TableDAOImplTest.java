package database.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import database.BaseIntegrationTest;
import model.TableOrderSummary;
import model.TableStatusView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableDAOImplTest extends BaseIntegrationTest {

    private TableDAOImpl dao;

    @BeforeEach
    void setUp() {
        dao = new TableDAOImpl();
    }

    @Test
    @DisplayName("全テーブルのステータスが取得できること")
    void testFindAllTableStatus() {
        List<TableStatusView> statuses = dao.findAllTableStatus();
        assertNotNull(statuses);
        // seed.sql で投入された 4 つのテーブルがあるはず
        assertEquals(4, statuses.size());
    }

    @Test
    @DisplayName("特定のテーブルの注文サマリーが取得できること")
    void testGetTableOrderSummary() {
        // ID:1 は 'A-1' (seed.sql)
        Optional<TableOrderSummary> summaryOpt = dao.getTableOrderSummary(1);
        assertTrue(summaryOpt.isPresent());
        TableOrderSummary summary = summaryOpt.get();
        assertEquals("A-1", summary.tableName());
        assertNotNull(summary.items());
    }

    @Test
    @DisplayName("存在しないテーブルIDを指定した場合は空のOptionalが返ること")
    void testGetTableOrderSummary_NotFound() {
        Optional<TableOrderSummary> summaryOpt = dao.getTableOrderSummary(999);
        assertFalse(summaryOpt.isPresent());
    }

    @Test
    @DisplayName("未精算のテーブル一覧が取得できること")
    void testFindUnsettledTables() {
        // 初期状態では注文がないため、空リストになるはず
        List<TableOrderSummary> unsettled = dao.findUnsettledTables();
        assertNotNull(unsettled);
        assertEquals(0, unsettled.size());
    }
}
