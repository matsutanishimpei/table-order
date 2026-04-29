package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC のボイラープレートコードを排除するためのユーティリティクラスです。
 * PreparedStatement の設定、例外処理、およびリソース管理を一括して行います。
 */
@Slf4j
public final class JdbcExecutor {

    private JdbcExecutor() { }

    /**
     * クエリを実行し、結果をリストとして返します。
     *
     * @param <T> マッピング後の型
     * @param con データベース接続
     * @param sql 実行する SQL
     * @param mapper 行マッパー
     * @param params プレースホルダにセットするパラメータ
     * @return マッピングされたオブジェクトのリスト
     */
    public static <T> List<T> query(Connection con, String sql, RowMapper<T> mapper, Object... params) {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                List<T> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(mapper.mapRow(rs));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Query execution failed: " + sql, e);
        }
    }

    /**
     * クエリを実行し、単一の結果を Optional として返します。
     */
    public static <T> Optional<T> queryOne(Connection con, String sql, RowMapper<T> mapper, Object... params) {
        List<T> list = query(con, sql, mapper, params);
        return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    /**
     * INSERT, UPDATE, DELETE を実行します。
     *
     * @param con データベース接続
     * @param sql 実行する SQL
     * @param params パラメータ
     * @return 影響を受けた行数
     */
    public static int update(Connection con, String sql, Object... params) {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            setParameters(ps, params);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Update failed: " + sql, e);
        }
    }

    /**
     * INSERT を実行し、自動生成された ID を返します。
     *
     * @param con データベース接続
     * @param sql 実行する SQL
     * @param params パラメータ
     * @return 生成された ID。失敗時は -1
     */
    public static int insertAndReturnId(Connection con, String sql, Object... params) {
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(ps, params);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new DatabaseException("Insert failed: " + sql, e);
        }
    }

    /**
     * 内部接続を使用するクエリ実行（自動接続管理版）。
     */
    public static <T> List<T> query(String sql, RowMapper<T> mapper, Object... params) {
        try (Connection con = DBManager.getConnection()) {
            return query(con, sql, mapper, params);
        } catch (SQLException e) {
            throw new DatabaseException("Connection acquisition failed for query", e);
        }
    }

    public static <T> Optional<T> queryOne(String sql, RowMapper<T> mapper, Object... params) {
        try (Connection con = DBManager.getConnection()) {
            return queryOne(con, sql, mapper, params);
        } catch (SQLException e) {
            throw new DatabaseException("Connection acquisition failed for queryOne", e);
        }
    }

    public static int update(String sql, Object... params) {
        try (Connection con = DBManager.getConnection()) {
            return update(con, sql, params);
        } catch (SQLException e) {
            throw new DatabaseException("Connection acquisition failed for update", e);
        }
    }

    /**
     * バッチ更新を実行します。
     *
     * @param con データベース接続
     * @param sql 実行する SQL
     * @param batchParams 各実行のパラメータリスト
     * @return 各実行で影響を受けた行数の配列
     */
    public static int[] batchUpdate(Connection con, String sql, List<Object[]> batchParams) {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Object[] params : batchParams) {
                setParameters(ps, params);
                ps.addBatch();
            }
            return ps.executeBatch();
        } catch (SQLException e) {
            throw new DatabaseException("Batch update failed: " + sql, e);
        }
    }

    private static void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }
}
