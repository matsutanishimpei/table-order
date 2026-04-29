package service;

import java.util.List;
import java.util.Optional;
import model.Product;

/**
 * 商品情報のビジネスロジックを定義するインターフェースです。
 * 商品の検索、登録、更新、および販売可否の制御を提供します。
 */
public interface ProductService {
    /**
     * すべての商品を取得します。
     *
     * @return 商品のリスト。存在しない場合は空のリストを返します。
     */
    List<Product> findAll();

    /**
     * 指定されたカテゴリに属する商品を取得します。
     *
     * @param categoryId カテゴリID
     * @return 該当する商品のリスト
     */
    List<Product> findByCategory(int categoryId);

    /**
     * 指定された ID の商品を取得します。
     *
     * @param productId 商品ID
     * @return 商品を保持する Optional インスタンス
     */
    Optional<Product> findById(int productId);

    /**
     * 新しい商品を登録します。
     *
     * @param p 登録する商品情報（名前、価格、カテゴリIDは必須）
     * @param operatorId 操作者のユーザーID
     * @return 登録に成功した場合は true
     * @throws exception.BusinessException バリデーションエラーが発生した場合
     */
    boolean insert(Product p, String operatorId);

    /**
     * 商品情報を更新します。
     *
     * @param p 更新する商品情報（IDは必須。名前、価格、カテゴリIDもバリデーション対象）
     * @param operatorId 操作者のユーザーID
     * @return 更新に成功した場合は true
     * @throws exception.BusinessException バリデーションエラーが発生した場合
     */
    boolean update(Product p, String operatorId);

    /**
     * 商品の販売可否（品切れ状態など）を更新します。
     *
     * @param productId 商品ID
     * @param isAvailable 販売可能な場合は true、品切れ等の場合は false
     * @param operatorId 操作者のユーザーID
     * @return 更新に成功した場合は true
     */
    boolean updateAvailability(int productId, boolean isAvailable, String operatorId);
}
