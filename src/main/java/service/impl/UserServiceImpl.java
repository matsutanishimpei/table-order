package service.impl;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import database.UserDAO;
import database.impl.UserDAOImpl;
import model.User;
import service.UserService;

/**
 * ユーザー管理業務のビジネスロジックを管理するService実装クラスです。
 */
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    // プロダクション用コンストラクタ
    public UserServiceImpl() {
        this(new UserDAOImpl());
    }

    // テスト・DI用コンストラクタ
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public Optional<User> findById(String id) {
        return userDAO.findById(id);
    }

    @Override
    public boolean register(User user) {
        try {
            boolean success = userDAO.insert(user);
            if (success) {
                log.info("ユーザー登録成功: id={}", user.id());
            } else {
                log.warn("ユーザー登録失敗: id={}", user.id());
            }
            return success;
        } catch (Exception e) {
            log.error("ユーザー登録エラー: id={}, reason={}", user.id(), e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(User user, String newPassword) {
        try {
            boolean success = userDAO.update(user);
            if (success && newPassword != null && !newPassword.isEmpty()) {
                success = userDAO.updatePassword(user.id(), newPassword);
            }
            if (success) {
                log.info("ユーザー更新成功: id={}", user.id());
            }
            return success;
        } catch (Exception e) {
            log.error("ユーザー更新エラー: id={}, reason={}", user.id(), e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try {
            boolean success = userDAO.delete(id);
            if (success) {
                log.info("ユーザー削除成功: id={}", id);
            }
            return success;
        } catch (Exception e) {
            log.error("ユーザー削除エラー: id={}, reason={}", id, e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<User> login(String id, String password) {
        return userDAO.login(id, password);
    }
}
