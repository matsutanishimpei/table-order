package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import exception.DatabaseException;

/**
 * JDBC トランザクションを管理するユーティリティクラスです。
 * 開発者が明示的に rollback や commit を書かずに済むようにします。
 */
public class TransactionManager {
    private static final Logger logger = Logger.getLogger(TransactionManager.class.getName());

    /**
     * 指定された処理をトランザクション制御下で実行します。
     * 正常終了時はコミット、例外発生時はロールバックを自動的に行います。
     * 
     * @param <T> 戻り値の型
     * @param executor 実行する処理 (Lambda式)
     * @return 処理結果
     */
    public static <T> T execute(TransactionExecutor<T> executor) {
        Connection con = null;
        try {
            con = DBManager.getConnection();
            con.setAutoCommit(false); // トランザクション開始

            T result = executor.execute(con);

            con.commit(); // 正常終了ならコミット
            return result;

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback(); // 例外発生ならロールバック
                    logger.warning("Transaction rolled back due to: " + e.getMessage());
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Rollback failed", ex);
                }
            }
            if (e instanceof DatabaseException) {
                throw (DatabaseException) e;
            }
            throw new DatabaseException("Transaction execution failed", e);
        } finally {
            DBManager.closeConnection(con);
        }
    }
}
