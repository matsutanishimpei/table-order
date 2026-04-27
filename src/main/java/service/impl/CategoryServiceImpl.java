package service.impl;

import java.util.List;

import database.CategoryDAO;
import database.impl.CategoryDAOImpl;
import model.Category;
import service.CategoryService;

/**
 * カテゴリ情報のビジネスロジックを管理するService実装クラスです。
 */
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDAO categoryDAO;

    // プロダクション用コンストラクタ
    public CategoryServiceImpl() {
        this(new CategoryDAOImpl());
    }

    // テスト・DI用コンストラクタ
    public CategoryServiceImpl(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    @Override
    public boolean insert(String name) {
        return categoryDAO.insert(name);
    }

    @Override
    public java.util.Optional<Category> findById(int id) {
        return categoryDAO.findById(id);
    }

    @Override
    public boolean update(Category category) {
        return categoryDAO.update(category);
    }
}
