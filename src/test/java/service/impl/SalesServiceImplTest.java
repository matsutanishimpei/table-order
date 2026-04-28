package service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import database.SalesDAO;
import model.DailySales;
import model.ProductSales;

@ExtendWith(MockitoExtension.class)
class SalesServiceImplTest {

    @Mock
    private SalesDAO salesDAO;

    private SalesServiceImpl salesService;

    @BeforeEach
    void setUp() {
        salesService = new SalesServiceImpl(salesDAO);
    }

    @Test
    @DisplayName("累計売上の取得: DAOの結果がそのまま返ること")
    void getTotalSales_ReturnsValue() {
        when(salesDAO.getTotalSales()).thenReturn(15000);
        assertEquals(15000, salesService.getTotalSales());
    }

    @Test
    @DisplayName("日次売上の取得: DAOの結果がそのまま返ること")
    void findDailySales_ReturnsList() {
        List<DailySales> expected = List.of(mock(DailySales.class));
        when(salesDAO.findDailySales()).thenReturn(expected);
        assertEquals(expected, salesService.findDailySales());
    }

    @Test
    @DisplayName("ランキングの取得: DAOの結果がそのまま返ること")
    void findProductSalesRanking_ReturnsList() {
        List<ProductSales> expected = List.of(mock(ProductSales.class));
        when(salesDAO.findProductSalesRanking()).thenReturn(expected);
        assertEquals(expected, salesService.findProductSalesRanking());
    }
}
