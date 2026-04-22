package database.impl;

import database.DBManager;
import database.ProductDAO;
import database.BaseDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Product;

/**
 * 商品情報のデータベース操作を行うDAO実装クラスです。
 */
public class ProductDAOImpl implements ProductDAO {

    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id, category_id, name, price, description, allergy_info, is_available FROM products ORDER BY id";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getInt("price"));
                p.setDescription(rs.getString("description"));
                p.setAllergyInfo(rs.getString("allergy_info"));
                p.setAvailable(rs.getBoolean("is_available"));
                list.add(p);
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("全商品取得中にエラーが発生しました。", e);
        }
        return list;
    }

    @Override
    public List<Product> findByCategory(int categoryId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id, category_id, name, price, description, allergy_info, is_available FROM products WHERE category_id = ? AND is_available = true ORDER BY id";

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
                    p.setDescription(rs.getString("description"));
                    p.setAllergyInfo(rs.getString("allergy_info"));
                    p.setAvailable(rs.getBoolean("is_available"));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("カテゴリ別商品取得中にエラーが発生しました。ID=" + categoryId, e);
        }
        return list;
    }

    @Override
    public Product findById(int productId) {
        Product p = null;
        String sql = "SELECT id, category_id, name, price, description, allergy_info, is_available FROM products WHERE id = ?";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setCategoryId(rs.getInt("category_id"));
                    p.setName(rs.getString("name"));
                    p.setPrice(rs.getInt("price"));
                    p.setDescription(rs.getString("description"));
                    p.setAllergyInfo(rs.getString("allergy_info"));
                    p.setAvailable(rs.getBoolean("is_available"));
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("商品取得中にエラーが発生しました。ID=" + productId, e);
        }
        return p;
    }

    @Override
    public boolean insert(Product p) {
        String sql = "INSERT INTO products (category_id, name, price, description, allergy_info, is_available) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setInt(3, p.getPrice());
            ps.setString(4, p.getDescription());
            ps.setString(5, p.getAllergyInfo());
            ps.setBoolean(6, p.isAvailable());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("商品登録中にエラーが発生しました。", e);
        }
    }

    @Override
    public boolean update(Product p) {
        String sql = "UPDATE products SET category_id = ?, name = ?, price = ?, description = ?, allergy_info = ?, is_available = ? WHERE id = ?";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setInt(3, p.getPrice());
            ps.setString(4, p.getDescription());
            ps.setString(5, p.getAllergyInfo());
            ps.setBoolean(6, p.isAvailable());
            ps.setInt(7, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("商品更新中にエラーが発生しました。ID=" + p.getId(), e);
        }
    }

    @Override
    public boolean updateAvailability(int productId, boolean isAvailable) {
        String sql = "UPDATE products SET is_available = ? WHERE id = ?";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, isAvailable);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("販売状態更新中にエラーが発生しました。ID=" + productId, e);
        }
    }
}
