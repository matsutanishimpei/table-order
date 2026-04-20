package model;

import java.io.Serializable;

/**
 * ユーザー情報を管理するモデルクラスです。
 * 総管理者とバイトを区別するための権限情報を持ちます。
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    // 権限管理用の定数
    public static final int ROLE_ADMIN = 1;     // 総管理者
    public static final int ROLE_KITCHEN = 2;   // キッチン
    public static final int ROLE_HALL = 3;      // ホール
    public static final int ROLE_CASHIER = 4;   // 会計
    public static final int ROLE_TABLE = 10;    // テーブル用端末

    private String id;       // ユーザーID
    private String password; // パスワード
    private int role;        // 権限
    private Integer tableId; // 座席ID (Table用)

    /**
     * デフォルトコンストラクタ
     */
    public User() {
    }

    /**
     * コンストラクタ
     */
    public User(String id, String password, int role, Integer tableId) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.tableId = tableId;
    }

    // ゲッター / セッター
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    /**
     * 総管理者であるかどうかを判定します。
     * @return 総管理者の場合はtrue、それ以外はfalse
     */
    public boolean isAdmin() {
        return this.role == ROLE_ADMIN;
    }

    /**
     * キッチン権限であるかどうかを判定します。
     */
    public boolean isKitchen() {
        return this.role == ROLE_KITCHEN;
    }

    /**
     * ホール権限であるかどうかを判定します。
     */
    public boolean isHall() {
        return this.role == ROLE_HALL;
    }

    /**
     * 会計権限であるかどうかを判定します。
     */
    public boolean isCashier() {
        return this.role == ROLE_CASHIER;
    }
}
