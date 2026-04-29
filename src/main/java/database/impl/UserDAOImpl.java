package database.impl;

import database.DBManager;
import database.JdbcExecutor;
import database.RowMapper;
import database.SqlConstants;
import database.UserDAO;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import model.User;
import util.PasswordUtil;

/**
 * ユーザー情報のデータベース操作を行うDAO実装クラスです。
 */
@Slf4j
public final class UserDAOImpl implements UserDAO {

    private final RowMapper<User> mapper = rs -> new User(
            rs.getString("id"),
            null, // パスワードは基本モデルには含めない
            rs.getInt("role"),
            (Integer) rs.getObject("table_id")
    );

    @Override
    public List<User> findAll() {
        return JdbcExecutor.query(SqlConstants.USER_SELECT_ALL, mapper);
    }

    @Override
    public Optional<User> login(String id, String password) {
        String sql = SqlConstants.USER_SELECT_LOGIN;
        String pepper = DBManager.getPepper();

        return JdbcExecutor.queryOne(sql, rs -> {
            String storedHash = rs.getString("password");
            if (storedHash != null && PasswordUtil.checkBcrypt(password, pepper, storedHash)) {
                return new User(
                        rs.getString("id"),
                        storedHash,
                        rs.getInt("role"),
                        (Integer) rs.getObject("table_id")
                );
            }
            return null;
        }, id).filter(user -> {
            if (user != null) {
                log.info("ログイン成功: ユーザーID={}", id);
                return true;
            }
            log.warn("ログイン失敗（パスワード不一致または形式不正）: ユーザーID={}", id);
            return false;
        });
    }

    @Override
    public Optional<User> findById(String id) {
        return JdbcExecutor.queryOne(SqlConstants.USER_SELECT_BY_ID, mapper, id);
    }

    @Override
    public boolean insert(User user, String operatorId) {
        String pepper = DBManager.getPepper();
        String hashedPassword = PasswordUtil.hashBcrypt(user.password(), pepper);
        return JdbcExecutor.update(SqlConstants.USER_INSERT,
                user.id(), hashedPassword, user.role(), user.tableId(), operatorId) > 0;
    }

    @Override
    public boolean update(User user, String operatorId) {
        return JdbcExecutor.update(SqlConstants.USER_UPDATE,
                user.role(), user.tableId(), operatorId, user.id()) > 0;
    }

    @Override
    public boolean updatePassword(String id, String newPassword, String operatorId) {
        String pepper = DBManager.getPepper();
        String hashedPassword = PasswordUtil.hashBcrypt(newPassword, pepper);
        return JdbcExecutor.update(SqlConstants.USER_UPDATE_PASSWORD,
                hashedPassword, operatorId, id) > 0;
    }

    @Override
    public boolean delete(String id) {
        return JdbcExecutor.update(SqlConstants.USER_DELETE, id) > 0;
    }
}
