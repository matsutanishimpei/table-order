package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.User;
import util.PasswordUtil;

/**
 * ユーザー情報のデータベース操作を行うDAOクラスです。
 */
public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    /**
     * ログイン認証を行い、ユーザー情報を取得します。
     * 
     * @param id ユーザーID
     * @param password 入力されたパスワード（平文）
     * @return 認証成功時はUserインスタンス、失敗した場合はnull
     */
    public User login(String id, String password) {
        User user = null;
        String sql = "SELECT id, password, salt, role, table_id FROM users WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String salt = rs.getString("salt");
                    String pepper = DBManager.getPepper();
                    
                    // ソルトとペッパーを使用してハッシュを計算（パスワード移行期を考慮しsalt有無で分岐）
                    String inputHash = (salt != null) ? PasswordUtil.hashPassword(password, salt, pepper) : password;

                    if (storedHash.equals(inputHash)) {
                        user = new User();
                        user.setId(rs.getString("id"));
                        user.setPassword(storedHash);
                        user.setRole(rs.getInt("role"));
                        user.setTableId((Integer) rs.getObject("table_id"));
                        
                        logger.info("ログイン成功: ユーザーID=" + id);
                    } else {
                        logger.warning("ログイン失敗（パスワード不一致）: ユーザーID=" + id);
                    }
                } else {
                    logger.warning("ログイン失敗（ユーザー未登録）: ユーザーID=" + id);
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ログイン処理中にデータベースエラーが発生しました。ID=" + id, e);
        }

        return user;
    }
}
