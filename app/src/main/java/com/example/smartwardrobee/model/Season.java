package com.example.smartwardrobee.model;

/**
 * 季节枚举
 */
public enum Season {
    SPRING("春季"),
    SUMMER("夏季"),
    AUTUMN("秋季"),
    WINTER("冬季"),
    ALL_SEASON("四季");
    
    private final String displayName;
    
    Season(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * 根据月份获取季节
     */
    public static Season getSeasonByMonth(int month) {
        switch (month) {
            case 3:
            case 4:
            case 5:
                return SPRING;
            case 6:
            case 7:
            case 8:
                return SUMMER;
            case 9:
            case 10:
            case 11:
                return AUTUMN;
            case 12:
            case 1:
            case 2:
                return WINTER;
            default:
                return ALL_SEASON;
        }
    }
    
    /**
     * 获取当前季节
     */
    public static Season getCurrentSeason() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int month = calendar.get(java.util.Calendar.MONTH) + 1; // Calendar.MONTH 从0开始
        return getSeasonByMonth(month);
    }
}
