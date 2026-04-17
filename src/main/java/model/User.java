package model;

import java.io.Serializable;

/**
 * ユーザー情報を管理するモデルクラスです。
 * 総管理者とバイトを区別するための権限情報を持ちます。
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    // 権限管理用の定数
    public static final int ROLE_ADMIN = 1; // 総管理者
    public static final int ROLE_STAFF = 2; // バイト

    private String id;       // ユーザーID
    private String password; // パスワード
    private int role;        // 権限 (1:総管理者, 2:バイト)

    /**
     * デフォルトコンストラクタ
     */
    public User() {
    }

    /**
     * コンストラクタ
     * 
     * @param id ユーザーID
     * @param password パスワード
     * @param role 権限
     */
    public User(String id, String password, int role) {
        this.id = id;
        this.password = password;
        this.role = role;
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

    /**
     * 総管理者であるかどうかを判定します。
     * @return 総管理者の場合はtrue、それ以外はfalse
     */
    public boolean isAdmin() {
        return this.role == ROLE_ADMIN;
    }

    /**
     * バイトであるかどうかを判定します。
     * @return バイトの場合はtrue、それ以外はfalse
     */
    public boolean isStaff() {
        return this.role == ROLE_STAFF;
    }
}
