package service.impl;

import java.util.List;
import java.util.logging.Logger;

import database.UserDAO;
import database.impl.UserDAOImpl;
import model.User;
import service.UserService;

/**
 * ユーザー管理業務のビジネスロジックを管理するService実装クラスです。
 */
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User findById(String id) {
        return userDAO.findById(id);
    }

    @Override
    public boolean register(User user) {
        try {
            boolean success = userDAO.insert(user);
            if (success) {
                logger.info("ユーザー登録成功: id=" + user.getId());
            } else {
                logger.warning("ユーザー登録失敗: id=" + user.getId());
            }
            return success;
        } catch (Exception e) {
            logger.severe("ユーザー登録エラー: id=" + user.getId() + ", reason=" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(User user, String newPassword) {
        try {
            boolean success = userDAO.update(user);
            if (success && newPassword != null && !newPassword.isEmpty()) {
                success = userDAO.updatePassword(user.getId(), newPassword);
            }
            if (success) {
                logger.info("ユーザー更新成功: id=" + user.getId());
            }
            return success;
        } catch (Exception e) {
            logger.severe("ユーザー更新エラー: id=" + user.getId() + ", reason=" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try {
            boolean success = userDAO.delete(id);
            if (success) {
                logger.info("ユーザー削除成功: id=" + id);
            }
            return success;
        } catch (Exception e) {
            logger.severe("ユーザー削除エラー: id=" + id + ", reason=" + e.getMessage());
            return false;
        }
    }

    @Override
    public User login(String id, String password) {
        return userDAO.login(id, password);
    }
}
