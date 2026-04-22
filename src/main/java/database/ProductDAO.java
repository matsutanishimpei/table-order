package database;

import java.util.List;
import model.Product;

/**
 * 商品情報のデータベース操作を行うDAOインターフェースです。
 */
public interface ProductDAO extends BaseDAO<model.Product> {
    /**
     * 指定されたカテゴリの販売中の商品を取得します。
     * @param categoryId カテゴリID
     * @return 商品リスト
     */
    List<Product> findByCategory(int categoryId);

    /**
     * 商品IDから商品を取得します。
     * @param id 商品ID
     * @return 商品情報
     */
    Product findById(int id);

    /**
     * 全ての商品を取得します。
     * @return 商品リスト
     */
    @Override
    List<Product> findAll();

    /**
     * 商品を新規登録します。
     * @param p 商品情報
     * @return 登録成功時は true
     */
    boolean insert(Product p);

    /**
     * 商品情報を更新します。
     * @param p 商品情報
     * @return 更新成功時は true
     */
    boolean update(Product p);

    /**
     * 商品の販売状態を更新します。
     * @param productId 商品ID
     * @param isAvailable 販売中かどうか
     * @return 更新成功時は true
     */
    boolean updateAvailability(int productId, boolean isAvailable);
}
