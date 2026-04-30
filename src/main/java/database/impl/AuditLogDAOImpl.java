package database.impl;

import database.AuditLogDAO;
import database.JdbcExecutor;
import database.SqlConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * 監査ログのデータベース操作を行うDAO実装クラスです。
 */
@Slf4j
public final class AuditLogDAOImpl implements AuditLogDAO {

    @Override
    public void log(String tableName, String recordId, String action,
                    String oldValue, String newValue, String operatorId) {
        try {
            JdbcExecutor.update(SqlConstants.AUDIT_LOG_INSERT,
                    tableName, recordId, action, oldValue, newValue, operatorId);
        } catch (Exception e) {
            // 監査ログの記録失敗は業務処理を止めない（ベストエフォート）
            log.error("監査ログの記録に失敗しました: table={}, recordId={}, action={}",
                    tableName, recordId, action, e);
        }
    }
}
