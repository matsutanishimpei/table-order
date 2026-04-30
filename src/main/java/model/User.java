package model;

import java.io.Serializable;

/**
 * ユーザー情報を管理するレコードです。
 *
 * @param id ユーザーID
 * @param password パスワード（ハッシュ）
 * @param role 権限
 * @param tableId テーブルID（テーブル端末の場合のみ）
 */
public record User(
        String id,
        String password,
        int role,
        Integer tableId,
        boolean isDeleted
) implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 初期値付きの引数なしコンストラクタ
     */
    public User() {
        this(null, null, 0, null, false);
    }

    /**
     * 管理者権限を持っているか判定します。
     */
    public boolean isAdmin() {
        return role == UserConstants.ROLE_ADMIN;
    }

    /**
     * キッチン権限を持っているか判定します。
     * 業務要件：管理者はキッチンの操作も可能とする。
     */
    public boolean isKitchen() {
        return role == UserConstants.ROLE_KITCHEN || isAdmin();
    }

    /**
     * ホール（配膳）権限を持っているか判定します。
     * 業務要件：管理者は配膳の操作も可能とする。
     */
    public boolean isHall() {
        return role == UserConstants.ROLE_HALL || isAdmin();
    }

    /**
     * 会計権限を持っているか判定します。
     * 業務要件：管理者は会計の操作も可能とする。
     */
    public boolean isCashier() {
        return role == UserConstants.ROLE_CASHIER || isAdmin();
    }

    /**
     * テーブル端末権限を持っているか判定します。
     */
    public boolean isTableTerminal() {
        return role == UserConstants.ROLE_TABLE_TERMINAL;
    }
}
