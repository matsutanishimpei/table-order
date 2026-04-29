package service.impl;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import database.UserDAO;
import database.impl.UserDAOImpl;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
    @SuppressFBWarnings("EI_EXPOSE_REP2")
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
    public boolean register(User user, String operatorId) {
        util.Validator.create()
            .required(user.id(), "ユーザーIDは必須です。")
            .maxLength(user.id(), 20, "ユーザーIDは20文字以内で入力してください。")
            .required(user.password(), "パスワードは必須です。")
            .throwOnErrors();

        // 重複チェック
        if (userDAO.findById(user.id()).isPresent()) {
            throw new exception.BusinessException("既に存在するIDです。id=" + user.id());
        }

        boolean success = userDAO.insert(user, operatorId);
        if (success) {
            log.info("ユーザー登録成功: id={}, operatorId={}", user.id(), operatorId);
        } else {
            log.warn("ユーザー登録失敗: id={}", user.id());
        }
        return success;
    }

    @Override
    public boolean update(User user, String newPassword, String operatorId) {
        util.Validator.create()
            .required(user.id(), "ユーザーIDは必須です。")
            .throwOnErrors();

        boolean success = userDAO.update(user, operatorId);
        if (success && newPassword != null && !newPassword.isEmpty()) {
            success = userDAO.updatePassword(user.id(), newPassword, operatorId);
        }
        if (success) {
            log.info("ユーザー更新成功: id={}, operatorId={}", user.id(), operatorId);
        }
        return success;
    }

    @Override
    public boolean delete(String id) {
        util.Validator.create()
            .required(id, "ユーザーIDは必須です。")
            .throwOnErrors();

        boolean success = userDAO.delete(id);
        if (success) {
            log.info("ユーザー削除成功: id={}", id);
        }
        return success;
    }

    @Override
    public Optional<User> login(String id, String password) {
        return userDAO.login(id, password);
    }
}
