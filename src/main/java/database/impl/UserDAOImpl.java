package database.impl;

import database.DBManager;
import database.SqlConstants;
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
        String sql = SqlConstants.USER_SELECT_ALL;
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new User(
                    rs.getString("id"),
                    null, // パスワードは一覧取得では不要
                    rs.getInt("role"),
                    (Integer) rs.getObject("table_id")
                ));
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("全ユーザー取得中にエラーが発生しました。", e);
        }
        return list;
    }

    @Override
    public Optional<User> login(String id, String password) {
        User user = null;
        String sql = SqlConstants.USER_SELECT_LOGIN;

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String pepper = DBManager.getPepper();
                    
                    // BCrypt による堅牢な認証のみをサポート
                    if (storedHash != null && PasswordUtil.checkBcrypt(password, pepper, storedHash)) {
                        user = new User(
                            rs.getString("id"),
                            storedHash,
                            rs.getInt("role"),
                            (Integer) rs.getObject("table_id")
                        );
                        log.info("ログイン成功: ユーザーID={}", id);
                    } else {
                        log.warn("ログイン失敗（パスワード不一致または形式不正）: ユーザーID={}", id);
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

    @Override
    public Optional<User> findById(String id) {
        User user = null;
        String sql = SqlConstants.USER_SELECT_BY_ID;
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                        rs.getString("id"),
                        null, // パスワードは不要
                        rs.getInt("role"),
                        (Integer) rs.getObject("table_id")
                    );
                }
            }
        } catch (SQLException e) {
            throw new exception.DatabaseException("ユーザー取得中にエラーが発生しました。ID=" + id, e);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public boolean insert(User user) {
        String sql = SqlConstants.USER_INSERT;
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String pepper = DBManager.getPepper();
            String hashedPassword = PasswordUtil.hashBcrypt(user.password(), pepper);

            ps.setString(1, user.id());
            ps.setString(2, hashedPassword);
            ps.setInt(3, user.role());
            ps.setObject(4, user.tableId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("ユーザー登録中にエラーが発生しました。ID=" + user.id(), e);
        }
    }

    @Override
    public boolean update(User user) {
        String sql = SqlConstants.USER_UPDATE;
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, user.role());
            ps.setObject(2, user.tableId());
            ps.setString(3, user.id());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("ユーザー更新中にエラーが発生しました。ID=" + user.id(), e);
        }
    }

    @Override
    public boolean updatePassword(String id, String newPassword) {
        String sql = SqlConstants.USER_UPDATE_PASSWORD;
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
        String sql = SqlConstants.USER_DELETE;
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new exception.DatabaseException("ユーザー削除中にエラーが発生しました。ID=" + id, e);
        }
    }
}
