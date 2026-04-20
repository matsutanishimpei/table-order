package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Product;

/**
 * 商品情報のデータベース操作を行うDAOクラスです。
 */
public class ProductDAO {
    private static final Logger logger = Logger.getLogger(ProductDAO.class.getName());
    /**
     * 指定されたカテゴリの販売中の商品を取得します。
     */
    public List<Product> findByCategory(int categoryId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id, category_id, name, price, image_path, is_available FROM products WHERE category_id = ? AND is_available = 1";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setCategoryId(rs.getInt("category_id"));
                    p.setName(rs.getString("name"));
                    p.setPrice(rs.getInt("price"));
                    p.setImagePath(rs.getString("image_path"));
                    p.setAvailable(rs.getBoolean("is_available"));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "カテゴリ別商品取得中にエラーが発生しました。categoryId=" + categoryId, e);
        }
        return list;
    }

    /**
     * 商品IDから商品を取得します。
     */
    public Product findById(int id) {
        Product p = null;
        String sql = "SELECT id, category_id, name, price, image_path, is_available FROM products WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setCategoryId(rs.getInt("category_id"));
                    p.setName(rs.getString("name"));
                    p.setPrice(rs.getInt("price"));
                    p.setImagePath(rs.getString("image_path"));
                    p.setAvailable(rs.getBoolean("is_available"));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "商品取得(findById)中にエラーが発生しました。id=" + id, e);
        }
        return p;
    }

    /**
     * 全ての商品を取得します（管理用）。
     */
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id, category_id, name, price, image_path, is_available FROM products ORDER BY id DESC";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getInt("price"));
                p.setImagePath(rs.getString("image_path"));
                p.setAvailable(rs.getBoolean("is_available"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 商品を新規登録します。
     */
    public boolean insert(Product p) {
        String sql = "INSERT INTO products (category_id, name, price, image_path, is_available) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setInt(3, p.getPrice());
            ps.setString(4, p.getImagePath());
            ps.setBoolean(5, p.isAvailable());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 商品の販売状態を更新します。
     */
    public boolean updateAvailability(int productId, boolean isAvailable) {
        String sql = "UPDATE products SET is_available = ? WHERE id = ?";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, isAvailable);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "商品状態更新中にエラーが発生しました。productId=" + productId, e);
            return false;
        }
    }
}
