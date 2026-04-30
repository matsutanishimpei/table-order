package database.impl;

import database.JdbcExecutor;
import database.RowMapper;
import database.SqlConstants;
import database.ProductDAO;

import java.util.List;
import java.util.Optional;

import model.Product;

/**
 * 商品情報のデータベース操作を行うDAO実装クラスです。
 */
public final class ProductDAOImpl implements ProductDAO {

    private final RowMapper<Product> mapper = rs -> new Product(
            rs.getInt("id"),
            rs.getInt("category_id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("description"),
            rs.getString("allergy_info"),
            rs.getString("image_path"),
            rs.getBoolean("is_available"),
            rs.getBoolean("is_deleted")
    );

    @Override
    public List<Product> findAll() {
        return JdbcExecutor.query(SqlConstants.PRODUCT_SELECT_ALL, mapper);
    }

    @Override
    public List<Product> findByCategory(int categoryId) {
        return JdbcExecutor.query(SqlConstants.PRODUCT_SELECT_BY_CATEGORY, mapper, categoryId);
    }

    @Override
    public Optional<Product> findById(int productId) {
        return JdbcExecutor.queryOne(SqlConstants.PRODUCT_SELECT_BY_ID, mapper, productId);
    }

    @Override
    public boolean insert(Product p, String operatorId) {
        return JdbcExecutor.update(SqlConstants.PRODUCT_INSERT,
                p.categoryId(), p.name(), p.price(), p.description(),
                p.allergyInfo(), p.imagePath(), p.isAvailable(), operatorId) > 0;
    }

    @Override
    public boolean update(Product p, String operatorId) {
        return JdbcExecutor.update(SqlConstants.PRODUCT_UPDATE,
                p.categoryId(), p.name(), p.price(), p.description(),
                p.allergyInfo(), p.imagePath(), p.isAvailable(), operatorId, p.id()) > 0;
    }

    @Override
    public boolean updateAvailability(int productId, boolean isAvailable, String operatorId) {
        return JdbcExecutor.update(SqlConstants.PRODUCT_UPDATE_AVAILABILITY, isAvailable, operatorId, productId) > 0;
    }

    @Override
    public boolean softDelete(int productId, String operatorId) {
        return JdbcExecutor.update(SqlConstants.PRODUCT_SOFT_DELETE, operatorId, productId) > 0;
    }
}
