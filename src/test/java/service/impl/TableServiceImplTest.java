package service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import database.TableDAO;
import model.OrderConstants;
import model.TableOrderSummary;
import model.TableStatusView;

@ExtendWith(MockitoExtension.class)
class TableServiceImplTest {

    @Mock
    private TableDAO tableDAO;

    private TableServiceImpl tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableServiceImpl(tableDAO);
    }

    @Test
    @DisplayName("未精算テーブル一覧の取得: DAOの結果がそのまま返ること")
    void findUnsettledTables_ReturnsList() {
        List<TableOrderSummary> expected = List.of(mock(TableOrderSummary.class));
        when(tableDAO.findUnsettledTables()).thenReturn(expected);

        List<TableOrderSummary> result = tableService.findUnsettledTables();

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("ステータスラベルの解決: ステータスコードに応じた日本語ラベルがセットされること")
    void findAllTableStatus_ResolvesLabels() {
        // Arrange
        TableStatusView v1 = new TableStatusView(1, "Table 1", null, null, 0, 0, null);
        TableStatusView v2 = new TableStatusView(2, "Table 2", null, String.valueOf(OrderConstants.STATUS_ORDERED), 1, 100, null);
        TableStatusView v3 = new TableStatusView(3, "Table 3", null, String.valueOf(OrderConstants.STATUS_COOKING_DONE), 1, 100, null);
        TableStatusView v4 = new TableStatusView(4, "Table 4", null, String.valueOf(OrderConstants.STATUS_SERVED), 1, 100, null);
        
        when(tableDAO.findAllTableStatus()).thenReturn(List.of(v1, v2, v3, v4));

        // Act
        List<TableStatusView> result = tableService.findAllTableStatus();

        // Assert
        assertEquals("空席", result.get(0).statusLabel());
        assertEquals("調理待ち", result.get(1).statusLabel());
        assertEquals("配膳待ち", result.get(2).statusLabel());
        assertEquals("食事中", result.get(3).statusLabel());
    }

    @Test
    @DisplayName("特定テーブルのサマリー取得: Optionalが返ること")
    void getTableOrderSummary_ReturnsOptional() {
        TableOrderSummary summary = mock(TableOrderSummary.class);
        when(tableDAO.getTableOrderSummary(1)).thenReturn(Optional.of(summary));

        Optional<TableOrderSummary> result = tableService.getTableOrderSummary(1);

        assertTrue(result.isPresent());
        assertEquals(summary, result.get());
    }
}
