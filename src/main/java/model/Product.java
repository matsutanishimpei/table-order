package model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 商品情報を管理するモデルクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int categoryId;
    private String name;
    private int price;
    private String description;
    private String allergyInfo;
    private String imagePath;
    private boolean isAvailable;
}
