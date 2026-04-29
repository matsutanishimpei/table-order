package database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ResultSet の現在の行をオブジェクトにマッピングするための関数型インターフェースです。
 *
 * @param <T> マッピング後の型
 */
@FunctionalInterface
public interface RowMapper<T> {
    /**
     * ResultSet の現在行をオブジェクトに変換します。
     *
     * @param rs ResultSet
     * @return 変換後のオブジェクト
     * @throws SQLException 抽出中にエラーが発生した場合
     */
    T mapRow(ResultSet rs) throws SQLException;
}
