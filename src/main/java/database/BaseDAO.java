package database;

import java.util.List;

/**
 * すべてのDAOの基底インターフェースです。
 * 共通のデータ取得メソッドを定義します。
 * 
 * @param <T> モデルクラスの型
 */
public interface BaseDAO<T> {
    /**
     * 全てのデータを取得します。
     * @return データのリスト
     */
    List<T> findAll();
}
