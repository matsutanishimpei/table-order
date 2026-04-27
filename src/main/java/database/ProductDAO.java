package database;

import java.util.List;
import java.util.Optional;
import model.Product;

/**
 * 商品情報のデータベース操作を行うDAOインターフェースです。
 * 商品データの検索、新規登録、更新、および販売可否の制御を行います。
 */
public interface ProductDAO {
    /**
     * 指定されたカテゴリに属する販売可能な商品を取得します。
     * @param categoryId カテゴリID
     * @return 商品情報のリスト。該当がない場合は空のリストを返します。
     */
    List<Product> findByCategory(int categoryId);

    /**
     * 指定された ID の商品情報を取得します。
     * @param id 商品ID
     * @return 商品情報を含む Optional インスタンス
     */
    Optional<Product> findById(int id);

    /**
     * システムに登録されているすべての商品情報を取得します（管理用）。
     * @return 商品情報のリスト
     */
    List<Product> findAll();

    /**
     * 新しい商品をデータベースに登録します。
     * @param p 登録する商品情報
     * @return 登録に成功した場合は true
     */
    boolean insert(Product p);

    /**
     * 既存の商品情報を更新します。
     * @param p 更新する商品情報（IDにより対象を特定）
     * @return 更新に成功した場合は true
     */
    boolean update(Product p);

    /**
     * 商品の販売可否状態（有効/無効）を更新します。
     * @param productId 商品ID
     * @param isAvailable 販売可能な場合は true、不可の場合は false
     * @return 更新に成功した場合は true
     */
    boolean updateAvailability(int productId, boolean isAvailable);
}
