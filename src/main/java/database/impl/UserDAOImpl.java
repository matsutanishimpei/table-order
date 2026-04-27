package database.impl;

import database.DBManager;
import database.UserDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import model.User;
import util.PasswordUtil;

/**
 * ユーザー情報のデータベース操作を行うDAO実装クラスです。
 */
@Slf4j
public class UserDAOImpl implements UserDAO {

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, role, table_id FROM users ORDER BY id";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getString("id"));
                u.setRole(rs.getInt("role"));
                u.setTableId((Integer) rs.getObject("table_id"));
                list.add(u);
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("全ユーザー取得中にエラーが発生しました。", e);
        }
        return list;
    }

    @Override
    public Optional<User> login(String id, String password) {
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
                        
                        log.info("ログイン成功: ユーザーID={}", id);
                    } else {
                        log.warn("ログイン失敗（パスワード不一致）: ユーザーID={}", id);
                    }
                } else {
                    log.warn("ログイン失敗（ユーザー未登録）: ユーザーID={}", id);
                }
            }

        } catch (SQLException e) {
            throw new exception.DatabaseException("ログイン処理中にデータベースエラーが発生しました。ID=" + id, e);
        }

        return Optional.ofNullable(user);
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
                log.info("パスワードの BCrypt 強制アップグレード（オンザフライ・マイグレーション）が完了しました。ユーザーID={}", userId);
            }
        } catch (SQLException e) {
            // マイグレーション自体が失敗しても、今回のログインセッションは継続させるためログ出力のみ
            log.warn("BCrypt マイグレーション中にエラーが発生しました。ユーザーID={}", userId, e);
        }
    }

    @Override
    public Optional<User> findById(String id) {
        User user = null;
        String sql = "SELECT id, role, table_id FROM users WHERE id = ?";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getString("id"));
                    user.setRole(rs.getInt("role"));
                    user.setTableId((Integer) rs.getObject("table_id"));
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("ユーザー取得中にエラーが発生しました。ID=" + id, e);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public boolean insert(User user) {
        String sql = "INSERT INTO users (id, password, role, table_id) VALUES (?, ?, ?, ?)";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String pepper = DBManager.getPepper();
            String hashedPassword = PasswordUtil.hashBcrypt(user.getPassword(), pepper);

            ps.setString(1, user.getId());
            ps.setString(2, hashedPassword);
            ps.setInt(3, user.getRole());
            ps.setObject(4, user.getTableId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("ユーザー登録中にエラーが発生しました。ID=" + user.getId(), e);
        }
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET role = ?, table_id = ? WHERE id = ?";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, user.getRole());
            ps.setObject(2, user.getTableId());
            ps.setString(3, user.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("ユーザー更新中にエラーが発生しました。ID=" + user.getId(), e);
        }
    }

    @Override
    public boolean updatePassword(String id, String newPassword) {
        String sql = "UPDATE users SET password = ?, salt = NULL WHERE id = ?";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String pepper = DBManager.getPepper();
            String hashedPassword = PasswordUtil.hashBcrypt(newPassword, pepper);

            ps.setString(1, hashedPassword);
            ps.setString(2, id);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("パスワード更新中にエラーが発生しました。ID=" + id, e);
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("ユーザー削除中にエラーが発生しました。ID=" + id, e);
        }
    }
}
