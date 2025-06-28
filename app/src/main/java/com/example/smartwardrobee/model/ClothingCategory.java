package com.example.smartwardrobee.model;

/**
 * 衣物大分类枚举
 */
public enum ClothingCategory {
    TOP("上装"),
    BOTTOM("下装"),
    SHOES("鞋类"),
    UNDERWEAR("内衣"),
    ACCESSORIES("配饰"),
    SPORTSWEAR("运动装"),
    OTHER("其他");
    
    private final String displayName;
    
    ClothingCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
