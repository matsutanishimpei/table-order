package database.impl;

import database.CategoryDAO;
import database.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Category;

/**
 * カテゴリ情報のデータベース操作を行うDAO実装クラスです。
 */
public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT id, name FROM categories ORDER BY id";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                list.add(c);
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("全カテゴリ取得中にエラーが発生しました。", e);
        }
        return list;
    }

    @Override
    public boolean insert(String name) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("カテゴリ登録中にエラーが発生しました。name=" + name, e);
        }
    }

    @Override
    public Optional<Category> findById(int id) {
        String sql = "SELECT id, name FROM categories WHERE id = ?";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category c = new Category();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    return Optional.of(c);
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("カテゴリ取得中にエラーが発生しました。id=" + id, e);
        }
        return Optional.empty();
    }

    @Override
    public boolean update(Category category) {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setInt(2, category.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("カテゴリ更新中にエラーが発生しました。id=" + category.getId(), e);
        }
    }
}
