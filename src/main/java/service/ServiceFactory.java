package service;

import service.impl.*;

/**
 * サービスインスタンスを提供するファクトリクラスです。
 * 依存性注入（DI）を管理し、実装クラスの隠蔽を行います。
 */
public class ServiceFactory {

    private static final CategoryService categoryService = new CategoryServiceImpl();
    private static final ProductService productService = new ProductServiceImpl();
    private static final OrderService orderService = new OrderServiceImpl();
    private static final TableService tableService = new TableServiceImpl();
    private static final UserService userService = new UserServiceImpl();
    private static final SalesService salesService = new SalesServiceImpl();

    /**
     * CategoryService の実装を返します。
     */
    public static CategoryService getCategoryService() {
        return categoryService;
    }

    /**
     * ProductService の実装を返します。
     */
    public static ProductService getProductService() {
        return productService;
    }

    /**
     * OrderService の実装を返します。
     */
    public static OrderService getOrderService() {
        return orderService;
    }

    /**
     * TableService の実装を返します。
     */
    public static TableService getTableService() {
        return tableService;
    }

    /**
     * UserService の実装を返します。
     */
    public static UserService getUserService() {
        return userService;
    }

    /**
     * SalesService の実装を返します。
     */
    public static SalesService getSalesService() {
        return salesService;
    }
}
