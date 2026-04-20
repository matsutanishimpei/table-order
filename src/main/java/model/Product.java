package model;

import java.io.Serializable;

/**
 * 商品情報を管理するモデルクラスです。
 */
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

    public Product() {}

    public Product(int id, int categoryId, String name, int price, String description, String allergyInfo, String imagePath, boolean isAvailable) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.allergyInfo = allergyInfo;
        this.imagePath = imagePath;
        this.isAvailable = isAvailable;
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAllergyInfo() { return allergyInfo; }
    public void setAllergyInfo(String allergyInfo) { this.allergyInfo = allergyInfo; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}
