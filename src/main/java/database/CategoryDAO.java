package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Category;

/**
 * カテゴリ情報のデータベース操作を行うDAOクラスです。
 */
public class CategoryDAO {
    private static final Logger logger = Logger.getLogger(CategoryDAO.class.getName());
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
            logger.log(Level.SEVERE, "全カテゴリ取得中にエラーが発生しました。", e);
        }
        return list;
    }

    /**
     * 新しいカテゴリーを登録します。
     */
    public boolean insert(String name) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "カテゴリ登録中にエラーが発生しました。name=" + name, e);
            return false;
        }
    }
}
