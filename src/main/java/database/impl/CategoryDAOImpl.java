package database.impl;

import database.CategoryDAO;
import database.JdbcExecutor;
import database.RowMapper;
import database.SqlConstants;

import java.util.List;
import java.util.Optional;

import model.Category;

/**
 * カテゴリ情報のデータベース操作を行うDAO実装クラスです。
 */
public class CategoryDAOImpl implements CategoryDAO {

    private final RowMapper<Category> mapper = rs -> new Category(
            rs.getInt("id"),
            rs.getString("name")
    );

    @Override
    public List<Category> findAll() {
        return JdbcExecutor.query(SqlConstants.CATEGORY_SELECT_ALL, mapper);
    }

    @Override
    public boolean insert(String name, String operatorId) {
        return JdbcExecutor.update(SqlConstants.CATEGORY_INSERT, name, operatorId) > 0;
    }

    @Override
    public Optional<Category> findById(int id) {
        return JdbcExecutor.queryOne(SqlConstants.CATEGORY_SELECT_BY_ID, mapper, id);
    }

    @Override
    public boolean update(Category category, String operatorId) {
        return JdbcExecutor.update(SqlConstants.CATEGORY_UPDATE, category.name(), operatorId, category.id()) > 0;
    }
}
