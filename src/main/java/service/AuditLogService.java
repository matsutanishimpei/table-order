package service;

/**
 * システムの操作履歴（監査ログ）を記録するためのサービスインターフェースです。
 */
public interface AuditLogService {
    /**
     * 操作ログを記録します。
     *
     * @param tableName 対象テーブル名
     * @param recordId 対象レコードの識別子
     * @param action 操作内容 (INSERT, UPDATE, DELETE, etc.)
     * @param oldValue 変更前の値（任意）
     * @param newValue 変更後の値（任意）
     * @param operatorId 操作実行者ID
     */
    void log(String tableName, String recordId, String action, String oldValue, String newValue, String operatorId);

    /**
     * トランザクション内で操作ログを記録します。
     *
     * @param con データベース接続
     * @param tableName 対象テーブル名
     * @param recordId 対象レコードの識別子
     * @param action 操作内容
     * @param oldValue 変更前の値
     * @param newValue 変更後の値
     * @param operatorId 操作実行者ID
     */
    void log(java.sql.Connection con, String tableName, String recordId, String action, String oldValue, String newValue, String operatorId);
}
