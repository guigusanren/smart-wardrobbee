package com.example.smartwardrobee.model;

/**
 * 场合枚举
 */
public enum Occasion {
    CASUAL("休闲"),
    WORK("工作"),
    FORMAL("正式"),
    PARTY("聚会"),
    SPORT("运动"),
    TRAVEL("旅行"),
    DATE("约会"),
    BUSINESS("商务"),
    WEDDING("婚礼"),
    INTERVIEW("面试"),
    SCHOOL("上学"),
    HOME("居家"),
    OUTDOOR("户外"),
    BEACH("海滩"),
    SHOPPING("购物"),
    DINNER("晚餐"),
    MEETING("会议"),
    VACATION("度假"),
    FESTIVAL("节日"),
    OTHER("其他");
    
    private final String displayName;
    
    Occasion(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * 是否为正式场合
     */
    public boolean isFormal() {
        return this == FORMAL || this == BUSINESS || this == WEDDING || 
               this == INTERVIEW || this == MEETING || this == DINNER;
    }
    
    /**
     * 是否为休闲场合
     */
    public boolean isCasual() {
        return this == CASUAL || this == HOME || this == SHOPPING || 
               this == TRAVEL || this == VACATION;
    }
    
    /**
     * 是否为运动场合
     */
    public boolean isSport() {
        return this == SPORT || this == OUTDOOR || this == BEACH;
    }
}
