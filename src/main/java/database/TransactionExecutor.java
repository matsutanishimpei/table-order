package database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * トランザクション内で実行される処理を定義する関数型インターフェースです。
 *
 * @param <T> 戻り値の型
 */
@FunctionalInterface
public interface TransactionExecutor<T> {
    /**
     * トランザクション制御下で実行する処理です。
     *
     * @param con データベースコネクション
     * @return 処理結果
     * @throws SQLException データベースエラーが発生した場合
     */
    T execute(Connection con) throws SQLException;
}
