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
}
