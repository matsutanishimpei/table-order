package service;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import static org.junit.jupiter.api.Assertions.*;

class ServiceFactoryTest {

    @Test
    void testConstructorIsPrivate() throws NoSuchMethodException {
        Constructor<ServiceFactory> constructor = ServiceFactory.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // Should not happen if setAccessible is used
        }
    }

    @Test
    void testGetCategoryService() {
        assertNotNull(ServiceFactory.getCategoryService());
        assertTrue(ServiceFactory.getCategoryService() instanceof service.impl.CategoryServiceImpl);
    }

    @Test
    void testGetProductService() {
        assertNotNull(ServiceFactory.getProductService());
        assertTrue(ServiceFactory.getProductService() instanceof service.impl.ProductServiceImpl);
    }

    @Test
    void testGetOrderService() {
        assertNotNull(ServiceFactory.getOrderService());
        assertTrue(ServiceFactory.getOrderService() instanceof service.impl.OrderServiceImpl);
    }

    @Test
    void testGetTableService() {
        assertNotNull(ServiceFactory.getTableService());
        assertTrue(ServiceFactory.getTableService() instanceof service.impl.TableServiceImpl);
    }

    @Test
    void testGetUserService() {
        assertNotNull(ServiceFactory.getUserService());
        assertTrue(ServiceFactory.getUserService() instanceof service.impl.UserServiceImpl);
    }

    @Test
    void testGetSalesService() {
        assertNotNull(ServiceFactory.getSalesService());
        assertTrue(ServiceFactory.getSalesService() instanceof service.impl.SalesServiceImpl);
    }
}
