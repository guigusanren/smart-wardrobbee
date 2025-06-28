package com.example.smartwardrobee.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.smartwardrobee.model.*;

import java.util.Date;
import java.util.List;

/**
 * 衣物数据访问对象
 * 定义数据库操作方法
 */
@Dao
public interface ClothingDao {
    
    // 基础CRUD操作
    @Insert
    long insert(ClothingItem item);
    
    @Insert
    void insertAll(List<ClothingItem> items);
    
    @Update
    void update(ClothingItem item);
    
    @Delete
    void delete(ClothingItem item);
    
    @Query("DELETE FROM clothing_items WHERE id = :id")
    void deleteById(long id);
    
    @Query("DELETE FROM clothing_items")
    void deleteAll();
    
    // 查询操作
    @Query("SELECT * FROM clothing_items WHERE id = :id")
    LiveData<ClothingItem> getById(long id);
    
    @Query("SELECT * FROM clothing_items ORDER BY name ASC")
    LiveData<List<ClothingItem>> getAllClothing();
    
    @Query("SELECT * FROM clothing_items WHERE name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'")
    LiveData<List<ClothingItem>> searchClothing(String searchQuery);
    
    // 按类型筛选
    @Query("SELECT * FROM clothing_items WHERE type = :type ORDER BY name ASC")
    LiveData<List<ClothingItem>> getByType(ClothingType type);
    
    // 按季节筛选
    @Query("SELECT * FROM clothing_items WHERE seasons LIKE '%' || :season || '%' ORDER BY name ASC")
    LiveData<List<ClothingItem>> getBySeason(String season);
    
    // 按颜色筛选
    @Query("SELECT * FROM clothing_items WHERE color = :color OR colors LIKE '%' || :color || '%' ORDER BY name ASC")
    LiveData<List<ClothingItem>> getByColor(String color);
    
    // 按状态筛选
    @Query("SELECT * FROM clothing_items WHERE status = :status ORDER BY name ASC")
    LiveData<List<ClothingItem>> getByStatus(ClothingStatus status);
    
    // 获取收藏的衣物
    @Query("SELECT * FROM clothing_items WHERE isFavorite = 1 ORDER BY name ASC")
    LiveData<List<ClothingItem>> getFavorites();
    
    // 获取最近穿过的衣物
    @Query("SELECT * FROM clothing_items WHERE lastWornDate IS NOT NULL ORDER BY lastWornDate DESC LIMIT :limit")
    LiveData<List<ClothingItem>> getRecentlyWorn(int limit);
    
    // 获取最常穿的衣物
    @Query("SELECT * FROM clothing_items WHERE wearCount > 0 ORDER BY wearCount DESC LIMIT :limit")
    LiveData<List<ClothingItem>> getMostWorn(int limit);
    
    // 获取很少穿的衣物
    @Query("SELECT * FROM clothing_items WHERE wearCount <= :maxWearCount ORDER BY wearCount ASC, purchaseDate ASC")
    LiveData<List<ClothingItem>> getLeastWorn(int maxWearCount);
    
    // 统计查询
    @Query("SELECT COUNT(*) FROM clothing_items")
    LiveData<Integer> getTotalCount();
    
    @Query("SELECT COUNT(*) FROM clothing_items WHERE type = :type")
    LiveData<Integer> getCountByType(ClothingType type);
    
    @Query("SELECT COUNT(*) FROM clothing_items WHERE status = :status")
    LiveData<Integer> getCountByStatus(ClothingStatus status);
    
    @Query("SELECT AVG(wearCount) FROM clothing_items")
    LiveData<Double> getAverageWearCount();
    
    @Query("SELECT SUM(price) FROM clothing_items WHERE price > 0")
    LiveData<Double> getTotalValue();
    
    // 复杂查询
    @Query("SELECT * FROM clothing_items WHERE " +
           "(:type IS NULL OR type = :type) AND " +
           "(:color IS NULL OR color = :color OR colors LIKE '%' || :color || '%') AND " +
           "(:season IS NULL OR seasons LIKE '%' || :season || '%') AND " +
           "(:status IS NULL OR status = :status) AND " +
           "(:isFavorite IS NULL OR isFavorite = :isFavorite) " +
           "ORDER BY name ASC")
    LiveData<List<ClothingItem>> getFilteredClothing(
        ClothingType type, 
        String color, 
        String season, 
        ClothingStatus status, 
        Boolean isFavorite
    );
    
    // 更新操作
    @Query("UPDATE clothing_items SET wearCount = wearCount + 1, lastWornDate = :date WHERE id = :id")
    void incrementWearCount(long id, Date date);
    
    @Query("UPDATE clothing_items SET isFavorite = :isFavorite WHERE id = :id")
    void updateFavoriteStatus(long id, boolean isFavorite);
    
    @Query("UPDATE clothing_items SET status = :status WHERE id = :id")
    void updateStatus(long id, ClothingStatus status);
    
    @Query("UPDATE clothing_items SET rating = :rating WHERE id = :id")
    void updateRating(long id, int rating);
    
    // 获取所有不同的颜色
    @Query("SELECT DISTINCT color FROM clothing_items WHERE color IS NOT NULL AND color != '' ORDER BY color")
    LiveData<List<String>> getAllColors();
    
    // 获取所有不同的品牌
    @Query("SELECT DISTINCT brand FROM clothing_items WHERE brand IS NOT NULL AND brand != '' ORDER BY brand")
    LiveData<List<String>> getAllBrands();
    
    // 获取所有不同的材质
    @Query("SELECT DISTINCT material FROM clothing_items WHERE material IS NOT NULL AND material != '' ORDER BY material")
    LiveData<List<String>> getAllMaterials();
}
