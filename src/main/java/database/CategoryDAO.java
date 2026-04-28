package database;

import java.util.List;

/**
 * カテゴリ情報のデータベース操作を行うDAOインターフェースです。
 * 商品分類（ドリンク、フードなど）データの永続化を担います。
 */
public interface CategoryDAO {
    /**
     * システムに登録されている全てのカテゴリを取得します。
     *
     * @return カテゴリ情報のリスト。登録がない場合は空のリストを返します。
     */
    List<model.Category> findAll();

    /**
     * 新しいカテゴリーをデータベースに登録します。
     *
     * @param name カテゴリ名
     * @return 登録に成功した場合は true
     */
    boolean insert(String name);
    
    /**
     * 指定された ID のカテゴリ情報を取得します。
     *
     * @param id カテゴリID
     * @return カテゴリ情報を含む Optional インスタンス
     */
    java.util.Optional<model.Category> findById(int id);

    /**
     * 既存のカテゴリ情報を更新します。
     *
     * @param category 更新するカテゴリ情報（IDにより対象を特定）
     * @return 更新に成功した場合は true
     */
    boolean update(model.Category category);
}
