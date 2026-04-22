package database;

import java.util.List;
import model.Category;

/**
 * カテゴリ情報のデータベース操作を行うDAOインターフェースです。
 */
public interface CategoryDAO extends BaseDAO<model.Category> {
    /**
     * 全てのカテゴリを取得します。
     * @return カテゴリのリスト
     */
    @Override
    List<model.Category> findAll();

    /**
     * 新しいカテゴリーを登録します。
     * @param name カテゴリ名
     * @return 登録成功時は true
     */
    boolean insert(String name);
}
