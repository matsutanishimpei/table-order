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
                    
                    boolean isAuthenticated = false;

                    if (storedHash != null && storedHash.startsWith("$2a$")) {
                        // 【新規】BCrypt による堅牢な認証
                        isAuthenticated = PasswordUtil.checkBcrypt(password, pepper, storedHash);
                    } else {
                        // 【後方互換】過去のセキュアでないハッシュ方式での認証
                        String legacyInputHash = (salt != null) ? PasswordUtil.hashLegacy(password, salt, pepper) : password;
                        
                        // タイミング攻撃対策の定数時間比較を使用
                        if (PasswordUtil.isEqualConstantTime(storedHash, legacyInputHash)) {
                            isAuthenticated = true;
                            
                            // ★ ログイン成功のタイミングで、安全なBCrypt形式へオンザフライ移行する ★
                            String newBcryptHash = PasswordUtil.hashBcrypt(password, pepper);
                            upgradeToBcrypt(id, newBcryptHash);
                        }
                    }

                    if (isAuthenticated) {
                        user = new User();
                        user.setId(rs.getString("id"));
                        user.setPassword(storedHash); // TODO: BCrypt移行直後の値は古いままセットされるが、認証後には利用されないため問題なし
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

    /**
     * 旧パスワード形式から BCrypt へオンザフライで移行します。
     * 移行完了後は無用となる salt カラムも NULL にクリアします。
     * 
     * @param userId 対象のユーザーID
     * @param newBcryptHash 新しく生成された BCrypt ハッシュ
     */
    private void upgradeToBcrypt(String userId, String newBcryptHash) {
        String sql = "UPDATE users SET password = ?, salt = NULL WHERE id = ?";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, newBcryptHash);
            ps.setString(2, userId);
            
            if (ps.executeUpdate() > 0) {
                logger.info("パスワードの BCrypt 強制アップグレード（オンザフライ・マイグレーション）が完了しました。ユーザーID=" + userId);
            }
        } catch (SQLException e) {
            // マイグレーション自体が失敗しても、今回のログインセッションは継続させるためログ出力のみ
            logger.log(Level.WARNING, "BCrypt マイグレーション中にエラーが発生しました。ユーザーID=" + userId, e);
        }
    }
}
