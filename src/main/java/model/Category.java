package model;

import java.io.Serializable;

/**
 * 商品カテゴリーを管理するレコードです。
 * 
 * @param id カテゴリーID
 * @param name カテゴリー名
 */
public record Category(
    int id,
    String name
) implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 初期値付きの引数なしコンストラクタ
     */
    public Category() {
        this(0, null);
    }
}
