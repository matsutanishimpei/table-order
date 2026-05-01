package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 監査ログ情報を管理するレコードです。
 * 「いつ・誰が・どのテーブルの・どのレコードを・どのように変更したか」を記録します。
 *
 * @param id ログID
 * @param tableName 対象テーブル名
 * @param recordId 対象レコードのID
 * @param action 操作種別（INSERT / UPDATE / DELETE 等）
 * @param oldValue 変更前の値（JSON形式等）
 * @param newValue 変更後の値（JSON形式等）
 * @param operatedBy 操作者のユーザーID
 * @param operatedAt 操作日時
 */
public record AuditLog(
        long id,
        String tableName,
        String recordId,
        String action,
        String oldValue,
        String newValue,
        String operatedBy,
        Timestamp operatedAt
) implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ。可変オブジェクトであるTimestampの防御的コピーを作成します。
     */
    public AuditLog {
        if (operatedAt != null) {
            operatedAt = new Timestamp(operatedAt.getTime());
        }
    }

    /**
     * 操作日時のコピーを取得します。
     *
     * @return 操作日時の防御的コピー
     */
    @Override
    public Timestamp operatedAt() {
        if (this.operatedAt != null) {
            return new Timestamp(this.operatedAt.getTime());
        }
        return null;
    }
}
