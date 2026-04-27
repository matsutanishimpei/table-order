package database;

import java.util.List;

/**
 * カテゴリ情報のデータベース操作を行うDAOインターフェースです。
 */
public interface CategoryDAO {
    /**
     * 全てのカテゴリを取得します。
     * @return カテゴリのリスト
     */
    List<model.Category> findAll();

    /**
     * 新しいカテゴリーを登録します。
     * @param name カテゴリ名
     * @return 登録成功時は true
     */
    boolean insert(String name);
    
    /**
     * 指定されたIDのカテゴリを取得します。
     * @param id カテゴリID
     * @return カテゴリ（存在しない場合は empty）
     */
    java.util.Optional<model.Category> findById(int id);

    /**
     * カテゴリ情報を更新します。
     * @param category カテゴリ情報
     * @return 更新成功時は true
     */
    boolean update(model.Category category);
}
