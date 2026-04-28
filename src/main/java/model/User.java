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
        Integer tableId
) implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 初期値付きの引数なしコンストラクタ
     */
    public User() {
        this(null, null, 0, null);
    }

    /**
     * 管理者権限を持っているか判定します。
     */
    public boolean isAdmin() {
        return role == UserConstants.ROLE_ADMIN;
    }

    /**
     * キッチン権限を持っているか判定します。
     */
    public boolean isKitchen() {
        return role == UserConstants.ROLE_KITCHEN;
    }

    /**
     * ホール（配膳）権限を持っているか判定します。
     */
    public boolean isHall() {
        return role == UserConstants.ROLE_HALL;
    }

    /**
     * 会計権限を持っているか判定します。
     */
    public boolean isCashier() {
        return role == UserConstants.ROLE_CASHIER;
    }

    /**
     * テーブル端末権限を持っているか判定します。
     */
    public boolean isTableTerminal() {
        return role == UserConstants.ROLE_TABLE_TERMINAL;
    }
}
