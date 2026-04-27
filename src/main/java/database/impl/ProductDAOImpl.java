package database.impl;

import database.DBManager;
import database.SqlConstants;
import database.ProductDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Product;

/**
 * 商品情報のデータベース操作を行うDAO実装クラスです。
 */
public class ProductDAOImpl implements ProductDAO {

    /**
     * ResultSet の現在行から Product オブジェクトを生成します。
     */
    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
            rs.getInt("id"),
            rs.getInt("category_id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("description"),
            rs.getString("allergy_info"),
            rs.getString("image_path"),
            rs.getBoolean("is_available")
        );
    }

    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = SqlConstants.PRODUCT_SELECT_ALL;
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("全商品取得中にエラーが発生しました。", e);
        }
        return list;
    }

    @Override
    public List<Product> findByCategory(int categoryId) {
        List<Product> list = new ArrayList<>();
        String sql = SqlConstants.PRODUCT_SELECT_BY_CATEGORY;

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("カテゴリ別商品取得中にエラーが発生しました。ID=" + categoryId, e);
        }
        return list;
    }

    @Override
    public Optional<Product> findById(int productId) {
        Product p = null;
        String sql = SqlConstants.PRODUCT_SELECT_BY_ID;
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("商品取得中にエラーが発生しました。ID=" + productId, e);
        }
        return Optional.ofNullable(p);
    }

    @Override
    public boolean insert(Product p) {
        String sql = SqlConstants.PRODUCT_INSERT;
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.categoryId());
            ps.setString(2, p.name());
            ps.setInt(3, p.price());
            ps.setString(4, p.description());
            ps.setString(5, p.allergyInfo());
            ps.setString(6, p.imagePath());
            ps.setBoolean(7, p.isAvailable());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("商品登録中にエラーが発生しました。", e);
        }
    }

    @Override
    public boolean update(Product p) {
        String sql = SqlConstants.PRODUCT_UPDATE;
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.categoryId());
            ps.setString(2, p.name());
            ps.setInt(3, p.price());
            ps.setString(4, p.description());
            ps.setString(5, p.allergyInfo());
            ps.setString(6, p.imagePath());
            ps.setBoolean(7, p.isAvailable());
            ps.setInt(8, p.id());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("商品更新中にエラーが発生しました。ID=" + p.id(), e);
        }
    }

    @Override
    public boolean updateAvailability(int productId, boolean isAvailable) {
        String sql = SqlConstants.PRODUCT_UPDATE_AVAILABILITY;
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
