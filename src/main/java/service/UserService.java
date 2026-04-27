package service;

import java.util.List;
import java.util.Optional;
import model.User;

/**
 * ユーザー管理業務のビジネスロジックを定義するインターフェースです。
 * 認証、ユーザーの登録・更新・削除、および検索機能を提供します。
 */
public interface UserService {
    /**
     * システムに登録されている全てのユーザーを取得します。
     * @return ユーザーリスト。登録がない場合は空のリストを返します。
     */
    List<User> findAll();

    /**
     * 指定されたユーザーIDからユーザー情報を取得します。
     * @param id 取得対象のユーザーID
     * @return ユーザー情報を含む Optional インスタンス
     */
    Optional<User> findById(String id);

    /**
     * ユーザーをシステムに新規登録します。
     * パスワードはサービス層でハッシュ化されて保存されます。
     * @param user 登録するユーザー情報（ID、パスワード、ロールは必須）
     * @return 登録に成功した場合は true
     * @throws exception.BusinessException バリデーションエラー（ID重複、入力不正など）が発生した場合
     */
    boolean register(User user);

    /**
     * ユーザー情報を更新します。
     * ロールや座席（テーブル番号）の変更を反映します。
     * @param user 更新するユーザー情報（IDは必須）
     * @param newPassword 新しいパスワードを設定する場合に指定します。変更しない場合は null または空文字を指定してください。
     * @return 更新に成功した場合は true
     * @throws exception.BusinessException バリデーションエラーが発生した場合
     */
    boolean update(User user, String newPassword);

    /**
     * 指定されたユーザーを削除します。
     * @param id 削除対象のユーザーID
     * @return 削除に成功した場合は true（対象が存在しない場合は false）
     */
    boolean delete(String id);

    /**
     * ユーザーIDとパスワードによるログイン認証を行います。
     * 内部でパスワードの照合（ハッシュ比較）が行われます。
     * @param id ユーザーID
     * @param password 入力された生パスワード
     * @return 認証に成功した場合は User オブジェクトを含む Optional、失敗した場合は空の Optional
     */
    Optional<User> login(String id, String password);
}
