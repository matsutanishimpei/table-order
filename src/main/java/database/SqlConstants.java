package database;

/**
 * データベース操作で使用する SQL クエリの定数クラスです。
 */
public class SqlConstants {

    // --- Users ---
    public static final String USER_SELECT_ALL = "SELECT id, role, table_id FROM users ORDER BY id";
    public static final String USER_SELECT_BY_ID = "SELECT id, role, table_id FROM users WHERE id = ?";
    public static final String USER_SELECT_LOGIN = "SELECT id, password, role, table_id FROM users WHERE id = ?";
    public static final String USER_INSERT =
            "INSERT INTO users (id, password, role, table_id, updated_by) VALUES (?, ?, ?, ?, ?)";
    public static final String USER_UPDATE = "UPDATE users SET role = ?, table_id = ?, updated_by = ? WHERE id = ?";
    public static final String USER_UPDATE_PASSWORD = "UPDATE users SET password = ?, updated_by = ? WHERE id = ?";
    public static final String USER_DELETE = "DELETE FROM users WHERE id = ?";

    // --- Products ---
    public static final String PRODUCT_SELECT_ALL =
            "SELECT id, category_id, name, price, description, allergy_info, image_path, is_available, is_deleted "
            + "FROM products WHERE is_deleted = false ORDER BY id";
    public static final String PRODUCT_SELECT_ALL_INCLUDING_DELETED =
            "SELECT id, category_id, name, price, description, allergy_info, image_path, is_available, is_deleted "
            + "FROM products ORDER BY id";
    public static final String PRODUCT_SELECT_BY_CATEGORY =
            "SELECT id, category_id, name, price, description, allergy_info, image_path, is_available, is_deleted "
            + "FROM products WHERE category_id = ? AND is_available = true AND is_deleted = false ORDER BY id";
    public static final String PRODUCT_SELECT_BY_ID =
            "SELECT id, category_id, name, price, description, allergy_info, image_path, is_available, is_deleted "
            + "FROM products WHERE id = ?";
    public static final String PRODUCT_INSERT =
            "INSERT INTO products (category_id, name, price, description, "
            + "allergy_info, image_path, is_available, updated_by) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String PRODUCT_UPDATE =
            "UPDATE products SET category_id = ?, name = ?, price = ?, description = ?, allergy_info = ?, "
            + "image_path = ?, is_available = ?, updated_by = ? WHERE id = ?";
    public static final String PRODUCT_UPDATE_AVAILABILITY =
            "UPDATE products SET is_available = ?, updated_by = ? WHERE id = ?";
    public static final String PRODUCT_SOFT_DELETE =
            "UPDATE products SET is_deleted = true, updated_by = ? WHERE id = ?";

    // --- Categories ---
    public static final String CATEGORY_SELECT_ALL =
            "SELECT id, name, is_deleted FROM categories WHERE is_deleted = false ORDER BY id";
    public static final String CATEGORY_SELECT_ALL_INCLUDING_DELETED =
            "SELECT id, name, is_deleted FROM categories ORDER BY id";
    public static final String CATEGORY_SELECT_BY_ID =
            "SELECT id, name, is_deleted FROM categories WHERE id = ?";
    public static final String CATEGORY_INSERT = "INSERT INTO categories (name, updated_by) VALUES (?, ?)";
    public static final String CATEGORY_UPDATE = "UPDATE categories SET name = ?, updated_by = ? WHERE id = ?";
    public static final String CATEGORY_SOFT_DELETE =
            "UPDATE categories SET is_deleted = true, updated_by = ? WHERE id = ?";

    // --- Orders ---
    public static final String ORDER_INSERT = "INSERT INTO orders (table_id, status, updated_by) VALUES (?, ?, ?)";
    public static final String ORDER_SELECT_ACTIVE_ID =
            "SELECT id FROM orders WHERE table_id = ? AND status < ? FOR UPDATE";
    public static final String ORDER_ITEM_INSERT =
            "INSERT INTO order_items (order_id, product_id, quantity, unit_price, status, updated_by) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
    public static final String ORDER_ITEM_SELECT_VIEW_BASE =
            "SELECT oi.id, p.name as product_name, oi.quantity, st.table_name, oi.created_at, oi.status, oi.unit_price "
            + "FROM order_items oi "
            + "JOIN products p ON oi.product_id = p.id "
            + "JOIN orders o ON oi.order_id = o.id "
            + "JOIN shop_tables st ON o.table_id = st.id ";

    public static final String ORDER_ITEM_SELECT_ACTIVE =
            ORDER_ITEM_SELECT_VIEW_BASE + "WHERE oi.status = ? ORDER BY oi.created_at ASC";
    public static final String ORDER_ITEM_UPDATE_STATUS =
            "UPDATE order_items SET status = ?, updated_by = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    public static final String ORDER_ITEM_SELECT_STATUS = "SELECT status FROM order_items WHERE id = ?";
    public static final String ORDER_ITEMS_UPDATE_STATUS_FOR_CHECKOUT =
            "UPDATE order_items SET status = ?, updated_by = ? WHERE status < ? "
            + "AND order_id IN (SELECT id FROM orders WHERE table_id = ?)";
    public static final String ORDER_UPDATE_STATUS_FOR_CHECKOUT =
            "UPDATE orders SET status = ?, updated_by = ? WHERE status < ? AND table_id = ?";

    // --- Tables ---
    public static final String TABLE_SELECT_UNSETTLED =
            "SELECT st.id, st.table_name, COUNT(oi.id) as item_count, SUM(oi.quantity * oi.unit_price) as total_amt, "
            + "SUM(CASE WHEN oi.status < ? THEN 1 ELSE 0 END) as unserved_count "
            + "FROM shop_tables st "
            + "JOIN orders o ON st.id = o.table_id "
            + "JOIN order_items oi ON o.id = oi.order_id "
            + "WHERE oi.status < ? "
            + "GROUP BY st.id, st.table_name "
            + "ORDER BY st.id";

    public static final String TABLE_SELECT_NAME_BY_ID = "SELECT table_name FROM shop_tables WHERE id = ?";

    public static final String TABLE_SELECT_ITEMS_BY_TABLE_ID =
            "SELECT oi.id, p.name, oi.quantity, oi.unit_price, oi.status "
            + "FROM order_items oi "
            + "JOIN products p ON oi.product_id = p.id "
            + "JOIN orders o ON oi.order_id = o.id "
            + "WHERE o.table_id = ? AND oi.status < ? "
            + "ORDER BY oi.created_at";

    public static final String ORDER_ITEM_COUNT_UNSERVED =
            "SELECT COUNT(*) FROM order_items oi "
            + "JOIN orders o ON oi.order_id = o.id "
            + "WHERE o.table_id = ? AND oi.status < ?";

    public static final String TABLE_SELECT_ALL_STATUS =
            "SELECT st.id, st.table_name, MIN(oi.status) as min_status, "
            + "COUNT(oi.id) as item_count, SUM(oi.quantity * oi.unit_price) as total_amt, "
            + "MAX(oi.created_at) as last_order "
            + "FROM shop_tables st "
            + "LEFT JOIN orders o ON st.id = o.table_id AND o.status < ? "
            + "LEFT JOIN order_items oi ON o.id = oi.order_id AND oi.status < ? "
            + "GROUP BY st.id, st.table_name "
            + "ORDER BY st.id";

    // --- Sales ---
    public static final String SALES_SELECT_TOTAL =
            "SELECT SUM(quantity * unit_price) FROM order_items WHERE status = ?";

    public static final String SALES_SELECT_DAILY =
            "SELECT DATE(o.updated_at) as sales_date, SUM(quantity * unit_price) as amount, "
            + "COUNT(DISTINCT o.id) as order_count "
            + "FROM orders o JOIN order_items oi ON o.id = oi.order_id "
            + "WHERE o.status = ? "
            + "GROUP BY sales_date "
            + "ORDER BY sales_date DESC "
            + "LIMIT 7";

    public static final String SALES_SELECT_RANKING =
            "SELECT p.name, SUM(oi.quantity) as total_qty, SUM(oi.quantity * oi.unit_price) as total_amt "
            + "FROM order_items oi JOIN products p ON oi.product_id = p.id "
            + "WHERE oi.status = ? "
            + "GROUP BY p.name "
            + "ORDER BY total_qty DESC";

    // --- Audit Log ---
    public static final String AUDIT_LOG_INSERT =
            "INSERT INTO audit_log (table_name, record_id, action, old_value, new_value, operated_by) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    private SqlConstants() {
        // インスタンス化防止
    }
}
