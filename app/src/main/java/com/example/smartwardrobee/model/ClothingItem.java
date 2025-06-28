package com.example.smartwardrobee.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

/**
 * 衣物实体类
 * 包含衣物的所有基本信息
 */
@Entity(tableName = "clothing_items")
@TypeConverters({Converters.class})
public class ClothingItem {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    // 基本信息
    private String name;                // 衣物名称
    private String description;         // 描述
    private String imagePath;          // 图片路径
    private ClothingType type;         // 衣物类型
    private String brand;              // 品牌
    private double price;              // 价格
    private Date purchaseDate;         // 购买日期
    
    // 属性信息
    private String color;              // 主要颜色
    private List<String> colors;       // 所有颜色
    private String material;           // 材质
    private String size;               // 尺码
    private List<Season> seasons;      // 适合的季节
    private List<Occasion> occasions;  // 适合的场合
    private String style;              // 风格
    
    // 使用统计
    private int wearCount;             // 穿着次数
    private Date lastWornDate;         // 最后穿着日期
    private boolean isFavorite;        // 是否收藏
    private int rating;                // 评分 (1-5)
    
    // 状态信息
    private ClothingStatus status;     // 状态（可穿、需洗、需修等）
    private String location;           // 存放位置
    private List<String> tags;         // 自定义标签
    
    // 构造函数
    public ClothingItem() {}

    @Ignore
    public ClothingItem(String name, ClothingType type, String color) {
        this.name = name;
        this.type = type;
        this.color = color;
        this.wearCount = 0;
        this.isFavorite = false;
        this.rating = 0;
        this.status = ClothingStatus.AVAILABLE;
        this.purchaseDate = new Date();
    }
    
    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    
    public ClothingType getType() { return type; }
    public void setType(ClothingType type) { this.type = type; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public List<String> getColors() { return colors; }
    public void setColors(List<String> colors) { this.colors = colors; }
    
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    
    public List<Season> getSeasons() { return seasons; }
    public void setSeasons(List<Season> seasons) { this.seasons = seasons; }
    
    public List<Occasion> getOccasions() { return occasions; }
    public void setOccasions(List<Occasion> occasions) { this.occasions = occasions; }
    
    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }
    
    public int getWearCount() { return wearCount; }
    public void setWearCount(int wearCount) { this.wearCount = wearCount; }
    
    public Date getLastWornDate() { return lastWornDate; }
    public void setLastWornDate(Date lastWornDate) { this.lastWornDate = lastWornDate; }
    
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public ClothingStatus getStatus() { return status; }
    public void setStatus(ClothingStatus status) { this.status = status; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    // 便捷方法
    public void incrementWearCount() {
        this.wearCount++;
        this.lastWornDate = new Date();
    }
    
    public boolean isAvailable() {
        return status == ClothingStatus.AVAILABLE;
    }
    
    public boolean isSuitableForSeason(Season season) {
        return seasons != null && seasons.contains(season);
    }
    
    public boolean isSuitableForOccasion(Occasion occasion) {
        return occasions != null && occasions.contains(occasion);
    }
}
