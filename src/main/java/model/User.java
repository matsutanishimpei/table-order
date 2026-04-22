package model;

import java.io.Serializable;

/**
 * ユーザー情報を管理するモデルクラスです。
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String password;
    private int role;
    private Integer tableId;

    public User() {}

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

    // getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }
    public Integer getTableId() { return tableId; }
    public void setTableId(Integer tableId) { this.tableId = tableId; }
}
