package com.example.smartwardrobee.model;

/**
 * 衣物类型枚举
 */
public enum ClothingType {
    // 上装
    T_SHIRT("T恤"),
    SHIRT("衬衫"),
    BLOUSE("女式衬衫"),
    SWEATER("毛衣"),
    HOODIE("连帽衫"),
    JACKET("夹克"),
    COAT("外套"),
    BLAZER("西装外套"),
    CARDIGAN("开衫"),
    VEST("背心"),
    TANK_TOP("吊带"),
    
    // 下装
    JEANS("牛仔裤"),
    TROUSERS("长裤"),
    SHORTS("短裤"),
    SKIRT("裙子"),
    DRESS("连衣裙"),
    LEGGINGS("打底裤"),
    
    // 鞋类
    SNEAKERS("运动鞋"),
    DRESS_SHOES("正装鞋"),
    BOOTS("靴子"),
    SANDALS("凉鞋"),
    FLATS("平底鞋"),
    HEELS("高跟鞋"),
    SLIPPERS("拖鞋"),
    
    // 内衣
    UNDERWEAR("内衣"),
    SOCKS("袜子"),
    TIGHTS("丝袜"),
    
    // 配饰
    HAT("帽子"),
    SCARF("围巾"),
    GLOVES("手套"),
    BELT("腰带"),
    BAG("包包"),
    JEWELRY("首饰"),
    WATCH("手表"),
    SUNGLASSES("太阳镜"),
    
    // 运动装
    SPORTSWEAR("运动服"),
    SWIMWEAR("泳装"),
    
    // 其他
    PAJAMAS("睡衣"),
    UNIFORM("制服"),
    OTHER("其他");
    
    private final String displayName;
    
    ClothingType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * 获取衣物类型的分类
     */
    public ClothingCategory getCategory() {
        switch (this) {
            case T_SHIRT:
            case SHIRT:
            case BLOUSE:
            case SWEATER:
            case HOODIE:
            case JACKET:
            case COAT:
            case BLAZER:
            case CARDIGAN:
            case VEST:
            case TANK_TOP:
                return ClothingCategory.TOP;
                
            case JEANS:
            case TROUSERS:
            case SHORTS:
            case SKIRT:
            case DRESS:
            case LEGGINGS:
                return ClothingCategory.BOTTOM;
                
            case SNEAKERS:
            case DRESS_SHOES:
            case BOOTS:
            case SANDALS:
            case FLATS:
            case HEELS:
            case SLIPPERS:
                return ClothingCategory.SHOES;
                
            case UNDERWEAR:
            case SOCKS:
            case TIGHTS:
                return ClothingCategory.UNDERWEAR;
                
            case HAT:
            case SCARF:
            case GLOVES:
            case BELT:
            case BAG:
            case JEWELRY:
            case WATCH:
            case SUNGLASSES:
                return ClothingCategory.ACCESSORIES;
                
            case SPORTSWEAR:
            case SWIMWEAR:
                return ClothingCategory.SPORTSWEAR;
                
            default:
                return ClothingCategory.OTHER;
        }
    }
    
    /**
     * 是否为外套类型
     */
    public boolean isOuterwear() {
        return this == JACKET || this == COAT || this == BLAZER;
    }
    
    /**
     * 是否为正装类型
     */
    public boolean isFormal() {
        return this == SHIRT || this == BLOUSE || this == BLAZER || 
               this == TROUSERS || this == DRESS_SHOES || this == DRESS;
    }
}
