package service;

import java.util.List;
import model.User;

/**
 * ユーザー管理業務のビジネスロジックを定義するインターフェースです。
 */
public interface UserService {
    /**
     * 全てのユーザーを取得します。
     * @return ユーザーリスト
     */
    List<User> findAll();

    /**
     * ユーザーIDからユーザー情報を取得します。
     * @param id ユーザーID
     * @return ユーザー情報
     */
    User findById(String id);

    /**
     * ユーザーを新規登録します。
     * @param user ユーザー情報
     * @return 成功時は true
     */
    boolean register(User user);

    /**
     * ユーザー情報を更新します。
     * @param user ユーザー情報
     * @param newPassword 新しいパスワード（変更しない場合は null または空）
     * @return 成功時は true
     */
    boolean update(User user, String newPassword);

    /**
     * ユーザーを削除します。
     * @param id ユーザーID
     * @return 成功時は true
     */
    boolean delete(String id);

    /**
     * ログイン認証を行います。
     * @param id ユーザーID
     * @param password パスワード
     * @return 成功時は User、失敗時は null
     */
    User login(String id, String password);
}
