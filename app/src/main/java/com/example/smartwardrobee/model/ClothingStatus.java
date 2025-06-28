package com.example.smartwardrobee.model;

/**
 * 衣物状态枚举
 */
public enum ClothingStatus {
    AVAILABLE("可穿"),
    DIRTY("需洗"),
    WASHING("洗涤中"),
    DRYING("晾晒中"),
    NEED_REPAIR("需修补"),
    STORED("收纳中"),
    DONATED("已捐赠"),
    DISCARDED("已丢弃"),
    LOST("丢失"),
    BORROWED("借出"),
    SEASONAL_STORAGE("季节性收纳");
    
    private final String displayName;
    
    ClothingStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * 是否可以穿着
     */
    public boolean isWearable() {
        return this == AVAILABLE;
    }
    
    /**
     * 是否需要处理
     */
    public boolean needsAttention() {
        return this == DIRTY || this == NEED_REPAIR || this == LOST;
    }
    
    /**
     * 是否在处理中
     */
    public boolean isInProcess() {
        return this == WASHING || this == DRYING;
    }
}
