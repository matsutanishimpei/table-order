package database;

/**
 * 監査ログのデータベース操作を行うDAOインターフェースです。
 * データ変更履歴（「いつ・誰が・どの値を変更したか」）の記録を担います。
 */
public interface AuditLogDAO {

    /**
     * 監査ログを記録します。
     *
     * @param tableName 対象テーブル名（例: "products", "categories"）
     * @param recordId 対象レコードのID
     * @param action 操作種別（例: "INSERT", "UPDATE", "DELETE"）
     * @param oldValue 変更前の値（null 可）
     * @param newValue 変更後の値（null 可）
     * @param operatorId 操作者のユーザーID
     */
    void log(String tableName, String recordId, String action,
             String oldValue, String newValue, String operatorId);
}
