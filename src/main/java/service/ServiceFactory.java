package service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import service.impl.AuditLogServiceImpl;
import service.impl.CategoryServiceImpl;
import service.impl.OrderServiceImpl;
import service.impl.ProductServiceImpl;
import service.impl.SalesServiceImpl;
import service.impl.TableServiceImpl;
import service.impl.UserServiceImpl;

/**
 * サービスインスタンスを提供するファクトリクラスです。
 * 依存性注入（DI）を管理し、実装クラスの隠蔽を行います。
 */
public final class ServiceFactory {

    private static final CategoryService categoryService = new CategoryServiceImpl();
    private static final ProductService productService = new ProductServiceImpl();
    private static final OrderService orderService = new OrderServiceImpl();
    private static final TableService tableService = new TableServiceImpl();
    private static final UserService userService = new UserServiceImpl();
    private static final SalesService salesService = new SalesServiceImpl();
    private static final AuditLogService auditLogService = new AuditLogServiceImpl();

    /**
     * CategoryService の実装を返します。
     *
     * @return カテゴリ管理サービス
     */
    @SuppressFBWarnings("MS_EXPOSE_REP")
    public static CategoryService getCategoryService() {
        return categoryService;
    }

    /**
     * ProductService の実装を返します。
     *
     * @return 商品管理サービス
     */
    @SuppressFBWarnings("MS_EXPOSE_REP")
    public static ProductService getProductService() {
        return productService;
    }

    /**
     * OrderService の実装を返します。
     *
     * @return 注文管理サービス
     */
    @SuppressFBWarnings("MS_EXPOSE_REP")
    public static OrderService getOrderService() {
        return orderService;
    }

    /**
     * TableService の実装を返します。
     *
     * @return 座席状態管理サービス
     */
    @SuppressFBWarnings("MS_EXPOSE_REP")
    public static TableService getTableService() {
        return tableService;
    }

    /**
     * UserService の実装を返します。
     *
     * @return ユーザー認証・管理サービス
     */
    @SuppressFBWarnings("MS_EXPOSE_REP")
    public static UserService getUserService() {
        return userService;
    }

    /**
     * SalesService の実装を返します。
     *
     * @return 売上集計サービス
     */
    @SuppressFBWarnings("MS_EXPOSE_REP")
    public static SalesService getSalesService() {
        return salesService;
    }

    /**
     * AuditLogService の実装を返します。
     *
     * @return 監査ログサービス
     */
    @SuppressFBWarnings("MS_EXPOSE_REP")
    public static AuditLogService getAuditLogService() {
        return auditLogService;
    }

    private ServiceFactory() {
        // インスタンス化防止
    }
}
