package service;

import java.util.List;
import model.Category;

/**
 * カテゴリ情報のビジネスロジックを定義するインターフェースです。
 * 商品の分類（ドリンク、フードなど）に関する操作を提供します。
 */
public interface CategoryService {
    /**
     * すべてのカテゴリを取得します。
     *
     * @return カテゴリのリスト。存在しない場合は空のリストを返します。
     */
    List<Category> findAll();

    /**
     * 新しいカテゴリを登録します。
     *
     * @param name カテゴリ名（必須。トリム後の空文字は不可）
     * @param operatorId 操作者のユーザーID
     * @return 登録に成功した場合は true
     * @throws exception.BusinessException バリデーションエラーが発生した場合
     */
    boolean insert(String name, String operatorId);

    /**
     * 指定された ID のカテゴリを取得します。
     *
     * @param id カテゴリID
     * @return カテゴリを保持する Optional インスタンス
     */
    java.util.Optional<model.Category> findById(int id);

    /**
     * カテゴリ情報を更新します。
     *
     * @param category 更新するカテゴリ情報（ID必須、名前のトリム後の空文字不可）
     * @param operatorId 操作者のユーザーID
     * @return 更新に成功した場合は true
     * @throws exception.BusinessException バリデーションエラーが発生した場合
     */
    boolean update(model.Category category, String operatorId);

    /**
     * カテゴリを論理削除します。
     * 物理削除ではなく is_deleted フラグを true に設定し、データの整合性を保ちます。
     *
     * @param id カテゴリID
     * @param operatorId 操作者のユーザーID
     * @return 削除に成功した場合は true
     */
    boolean delete(int id, String operatorId);
}
