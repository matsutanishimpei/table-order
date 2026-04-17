package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

/**
 * ユーザー情報のデータベース操作を行うDAOクラスです。
 */
public class UserDAO {

    /**
     * ログイン認証を行い、ユーザー情報を取得します。
     * 
     * @param id ユーザーID
     * @param password パスワード
     * @return 認証成功時はUserインスタンス、失敗した場合はnull
     */
    public User login(String id, String password) {
        User user = null;
        String sql = "SELECT id, password, role FROM users WHERE id = ? AND password = ?";

        // DBManagerから接続を取得
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // SQLパラメータの設定
            ps.setString(1, id);
            ps.setString(2, password);

            // クエリの実行
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 検索結果がある場合、Userインスタンスを生成
                    user = new User();
                    user.setId(rs.getString("id"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getInt("role"));
                }
            }

        } catch (SQLException e) {
            System.err.println("ログイン処理中にエラーが発生しました。");
            e.printStackTrace();
        }

        return user;
    }
}
