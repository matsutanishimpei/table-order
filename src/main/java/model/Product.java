package model;

import java.io.Serializable;

/**
 * 商品情報を管理するレコードです。
 *
 * @param id 商品ID
 * @param categoryId カテゴリーID
 * @param name 商品名
 * @param price 価格
 * @param description 商品説明
 * @param allergyInfo アレルギー情報
 * @param imagePath 画像パス（識別子）
 * @param isAvailable 販売中フラグ
 */
public record Product(
        int id,
        int categoryId,
        String name,
        int price,
        String description,
        String allergyInfo,
        String imagePath,
        boolean isAvailable
) implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 新規登録用のコンストラクタ（ID=0）
     */
    public Product() {
        this(0, 0, null, 0, null, null, null, true);
    }
}
