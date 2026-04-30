package database;

import java.util.List;
import java.util.Optional;
import model.User;

/**
 * ユーザー情報のデータベース操作を行うDAOインターフェースです。
 */
public interface UserDAO {
    /**
     * 全てのユーザーを取得します。
     *
     * @return ユーザーリスト
     */
    List<User> findAll();

    /**
     * ログイン認証を行い、ユーザー情報を取得します。
     *
     * @param id ユーザーID
     * @param password 入力されたパスワード（平文）
     * @return 認証成功時は User インスタンスを含むOptional、失敗した場合は 空のOptional
     */
    Optional<User> login(String id, String password);

    /**
     * ユーザーIDからユーザー情報を取得します。
     *
     * @param id ユーザーID
     * @return ユーザー情報を含むOptional
     */
    Optional<User> findById(String id);

    /**
     * ユーザーを新規登録します。
     *
     * @param user ユーザー情報
     * @param operatorId 操作者のユーザーID
     * @return 登録成功時は true
     */
    boolean insert(User user, String operatorId);

    /**
     * ユーザー情報を更新します（パスワード以外）。
     *
     * @param user ユーザー情報
     * @param operatorId 操作者のユーザーID
     * @return 更新成功時は true
     */
    boolean update(User user, String operatorId);

    /**
     * ユーザーのパスワードを更新します。
     *
     * @param id ユーザーID
     * @param newPassword 新しいパスワード（平文）
     * @return 更新成功時は true
     */
    boolean updatePassword(String id, String newPassword, String operatorId);

    /**
     * ユーザーを論理削除します。
     *
     * @param id ユーザーID
     * @param operatorId 操作者のユーザーID
     * @return 削除成功時は true
     */
    boolean softDelete(String id, String operatorId);

    /**
     * ユーザーを物理削除します（主にテスト用）。
     *
     * @param id ユーザーID
     * @return 削除成功時は true
     */
    boolean delete(String id);
}
