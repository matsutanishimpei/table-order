package util;

/**
 * アプリケーション全体で使用する定数を管理するクラスです。
 */
public class AppConstants {

    // --- JSP View Paths ---
    public static final String VIEW_LOGIN = "/WEB-INF/view/login.jsp";
    public static final String VIEW_MENU = "/WEB-INF/view/menu.jsp";
    public static final String VIEW_CART = "/WEB-INF/view/cart.jsp"; // もしあれば
    public static final String VIEW_ORDER_HISTORY = "/WEB-INF/view/order_history.jsp";
    public static final String VIEW_ERROR = "/WEB-INF/view/error.jsp";
    
    public static final String VIEW_ADMIN_HOME = "/WEB-INF/view/admin_home.jsp";
    public static final String VIEW_ADMIN_PRODUCTS = "/WEB-INF/view/admin_products.jsp";
    public static final String VIEW_ADMIN_PRODUCT_EDIT = "/WEB-INF/view/admin_product_edit.jsp";
    public static final String VIEW_ADMIN_CATEGORIES = "/WEB-INF/view/admin_categories.jsp";
    public static final String VIEW_ADMIN_CATEGORY_EDIT = "/WEB-INF/view/admin_category_edit.jsp";
    public static final String VIEW_ADMIN_USERS = "/WEB-INF/view/admin_users.jsp";
    public static final String VIEW_ADMIN_USER_EDIT = "/WEB-INF/view/admin_user_edit.jsp";
    public static final String VIEW_ADMIN_SALES = "/WEB-INF/view/admin_sales.jsp";
    public static final String VIEW_ADMIN_ORDER_MONITOR = "/WEB-INF/view/admin_order_monitor.jsp";
    
    public static final String VIEW_KITCHEN = "/WEB-INF/view/kitchen.jsp";
    public static final String VIEW_HALL = "/WEB-INF/view/hall.jsp";
    public static final String VIEW_CASHIER = "/WEB-INF/view/cashier.jsp";

    // --- Session & Request Attribute Keys ---
    public static final String ATTR_USER = "user";
    public static final String ATTR_CART = "cart";
    public static final String ATTR_ERROR = "error";
    public static final String ATTR_ERROR_MESSAGE = "errorMessage";
    public static final String ATTR_EXCEPTION = "exception";
    public static final String ATTR_MESSAGE = "message";
    public static final String ATTR_CSRF_TOKEN = "csrf_token";
    
    public static final String ATTR_CATEGORY_LIST = "categoryList";
    public static final String ATTR_PRODUCT_LIST = "productList";
    public static final String ATTR_TABLE_LIST = "tableList";
    public static final String ATTR_SELECTED_CATEGORY_ID = "selectedCategoryId";
    public static final String ATTR_PRODUCT = "product";
    public static final String ATTR_CATEGORY = "category";
    public static final String ATTR_USER_LIST = "userList";
    public static final String ATTR_TOTAL_SALES = "totalSales";
    public static final String ATTR_DAILY_SALES = "dailySales";
    public static final String ATTR_PRODUCT_RANKING = "productRanking";
    public static final String ATTR_ACTIVE_ITEMS = "activeItems";
    public static final String ATTR_READY_ITEMS = "readyItems";
    public static final String ATTR_UNSETTLED_TABLES = "unsettledTables";
    public static final String ATTR_SELECTED_SUMMARY = "selectedSummary";
    public static final String ATTR_SUMMARY = "summary";

    // --- Redirect Paths ---
    public static final String REDIRECT_LOGIN = "Login";
    public static final String REDIRECT_MENU = "Menu";
    public static final String REDIRECT_HOME = "Home"; // 各ロールのHomeへの相対リダイレクト用
    public static final String REDIRECT_ADMIN_HOME = "Admin/Home"; // LoginServlet等から使用
    public static final String REDIRECT_KITCHEN_HOME = "Kitchen/Home"; // LoginServlet等から使用
    public static final String REDIRECT_HALL_HOME = "Hall/Home"; // LoginServlet等から使用
    public static final String REDIRECT_CASHIER_HOME = "Cashier/Home"; // LoginServlet等から使用
    
    public static final String REDIRECT_ADMIN_PRODUCT = "Product"; // ProductAdminServlet内でのリダイレクト用
    public static final String REDIRECT_ADMIN_CATEGORY = "Category"; // CategoryAdminServlet内でのリダイレクト用
    public static final String REDIRECT_ADMIN_USER = "User"; // UserAdminServlet内でのリダイレクト用

    // --- Validation & Others ---
    public static final int MAX_PRODUCT_NAME_LENGTH = 50;
    public static final int MAX_CATEGORY_NAME_LENGTH = 20;
    
    public static final java.util.List<String> ALLOWED_IMAGE_TYPES =
            java.util.List.of("image/jpeg", "image/png", "image/webp", "image/gif");

    private AppConstants() {
        // インスタンス化防止
    }
}
