package database;

/**
 * データベース操作で使用する SQL クエリの定数クラスです。
 */
public class SqlConstants {

    // --- Users ---
    public static final String USER_SELECT_ALL = "SELECT id, role, table_id FROM users ORDER BY id";
    public static final String USER_SELECT_BY_ID = "SELECT id, role, table_id FROM users WHERE id = ?";
    public static final String USER_SELECT_LOGIN = "SELECT id, password, salt, role, table_id FROM users WHERE id = ?";
    public static final String USER_INSERT = "INSERT INTO users (id, password, role, table_id) VALUES (?, ?, ?, ?)";
    public static final String USER_UPDATE = "UPDATE users SET role = ?, table_id = ? WHERE id = ?";
    public static final String USER_UPDATE_PASSWORD = "UPDATE users SET password = ?, salt = NULL WHERE id = ?";
    public static final String USER_DELETE = "DELETE FROM users WHERE id = ?";
    public static final String USER_UPGRADE_BCRYPT = "UPDATE users SET password = ?, salt = NULL WHERE id = ?";

    // --- Products ---
    public static final String PRODUCT_SELECT_ALL = "SELECT id, category_id, name, price, description, allergy_info, image_path, is_available FROM products ORDER BY id";
    public static final String PRODUCT_SELECT_BY_CATEGORY = "SELECT id, category_id, name, price, description, allergy_info, image_path, is_available FROM products WHERE category_id = ? AND is_available = true ORDER BY id";
    public static final String PRODUCT_SELECT_BY_ID = "SELECT id, category_id, name, price, description, allergy_info, image_path, is_available FROM products WHERE id = ?";
    public static final String PRODUCT_INSERT = "INSERT INTO products (category_id, name, price, description, allergy_info, image_path, is_available) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String PRODUCT_UPDATE = "UPDATE products SET category_id = ?, name = ?, price = ?, description = ?, allergy_info = ?, image_path = ?, is_available = ? WHERE id = ?";
    public static final String PRODUCT_UPDATE_AVAILABILITY = "UPDATE products SET is_available = ? WHERE id = ?";

    // --- Categories ---
    public static final String CATEGORY_SELECT_ALL = "SELECT id, name FROM categories ORDER BY id";
    public static final String CATEGORY_SELECT_BY_ID = "SELECT id, name FROM categories WHERE id = ?";
    public static final String CATEGORY_INSERT = "INSERT INTO categories (name) VALUES (?)";
    public static final String CATEGORY_UPDATE = "UPDATE categories SET name = ? WHERE id = ?";

    // --- Orders ---
    public static final String ORDER_INSERT = "INSERT INTO orders (table_id, status) VALUES (?, ?)";
    public static final String ORDER_ITEM_INSERT = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, status) VALUES (?, ?, ?, ?, ?)";
    public static final String ORDER_ITEM_SELECT_VIEW_BASE = 
        "SELECT oi.id, p.name as product_name, oi.quantity, st.table_name, oi.created_at, oi.status, oi.unit_price " +
        "FROM order_items oi " +
        "JOIN products p ON oi.product_id = p.id " +
        "JOIN orders o ON oi.order_id = o.id " +
        "JOIN shop_tables st ON o.table_id = st.id ";
    
    public static final String ORDER_ITEM_SELECT_ACTIVE = ORDER_ITEM_SELECT_VIEW_BASE + "WHERE oi.status = ? ORDER BY oi.created_at ASC";
    public static final String ORDER_ITEM_UPDATE_STATUS = "UPDATE order_items SET status = ? WHERE id = ?";
    public static final String ORDER_ITEMS_UPDATE_STATUS_FOR_CHECKOUT = "UPDATE order_items SET status = ? WHERE status < ? AND order_id IN (SELECT id FROM orders WHERE table_id = ?)";
    public static final String ORDER_UPDATE_STATUS_FOR_CHECKOUT = "UPDATE orders SET status = ? WHERE status < ? AND table_id = ?";

    private SqlConstants() {
        // インスタンス化防止
    }
}
