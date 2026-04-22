package model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザー情報を管理するモデルクラスです。
 */
@Data
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String password;
    private int role;
    private Integer tableId;

    /**
     * 管理者権限を持っているか判定します。
     * @return 管理者の場合は true
     */
    public boolean isAdmin() {
        return role == 1;
    }

    /**
     * キッチン権限を持っているか判定します。
     * @return キッチンの場合は true
     */
    public boolean isKitchen() {
        return role == 2;
    }

    /**
     * ホール（配膳）権限を持っているか判定します。
     * @return ホールの場合は true
     */
    public boolean isHall() {
        return role == 3;
    }

    /**
     * 会計権限を持っているか判定します。
     * @return 会計の場合は true
     */
    public boolean isCashier() {
        return role == 4;
    }

    public boolean isCashier() {
        return role == 4;
    }
}
