package service;

import java.util.List;
import model.Category;

/**
 * カテゴリ情報のビジネスロジックを定義するインターフェースです。
 */
public interface CategoryService {
    List<Category> findAll();
    boolean insert(String name);
    java.util.Optional<model.Category> findById(int id);
    boolean update(model.Category category);
}
