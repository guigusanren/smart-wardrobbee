package com.example.smartwardrobee.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.smartwardrobee.database.ClothingDao;
import com.example.smartwardrobee.database.WardrobeDatabase;
import com.example.smartwardrobee.model.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 衣物数据仓库
 * 管理数据访问逻辑，作为ViewModel和数据库之间的中介
 */
public class ClothingRepository {
    
    private ClothingDao clothingDao;
    private LiveData<List<ClothingItem>> allClothing;
    
    public ClothingRepository(Application application) {
        WardrobeDatabase database = WardrobeDatabase.getDatabase(application);
        clothingDao = database.clothingDao();
        allClothing = clothingDao.getAllClothing();
    }
    
    // 获取所有衣物
    public LiveData<List<ClothingItem>> getAllClothing() {
        return allClothing;
    }
    
    // 根据ID获取衣物
    public LiveData<ClothingItem> getClothingById(long id) {
        return clothingDao.getById(id);
    }
    
    // 搜索衣物
    public LiveData<List<ClothingItem>> searchClothing(String query) {
        return clothingDao.searchClothing(query);
    }
    
    // 按类型筛选
    public LiveData<List<ClothingItem>> getClothingByType(ClothingType type) {
        return clothingDao.getByType(type);
    }
    
    // 按季节筛选
    public LiveData<List<ClothingItem>> getClothingBySeason(Season season) {
        return clothingDao.getBySeason(season.name());
    }
    
    // 按颜色筛选
    public LiveData<List<ClothingItem>> getClothingByColor(String color) {
        return clothingDao.getByColor(color);
    }
    
    // 按状态筛选
    public LiveData<List<ClothingItem>> getClothingByStatus(ClothingStatus status) {
        return clothingDao.getByStatus(status);
    }
    
    // 获取收藏的衣物
    public LiveData<List<ClothingItem>> getFavoriteClothing() {
        return clothingDao.getFavorites();
    }
    
    // 获取最近穿过的衣物
    public LiveData<List<ClothingItem>> getRecentlyWornClothing(int limit) {
        return clothingDao.getRecentlyWorn(limit);
    }
    
    // 获取最常穿的衣物
    public LiveData<List<ClothingItem>> getMostWornClothing(int limit) {
        return clothingDao.getMostWorn(limit);
    }
    
    // 获取很少穿的衣物
    public LiveData<List<ClothingItem>> getLeastWornClothing(int maxWearCount) {
        return clothingDao.getLeastWorn(maxWearCount);
    }
    
    // 复杂筛选
    public LiveData<List<ClothingItem>> getFilteredClothing(
            ClothingType type, String color, Season season, 
            ClothingStatus status, Boolean isFavorite) {
        String seasonStr = season != null ? season.name() : null;
        return clothingDao.getFilteredClothing(type, color, seasonStr, status, isFavorite);
    }
    
    // 统计数据
    public LiveData<Integer> getTotalCount() {
        return clothingDao.getTotalCount();
    }
    
    public LiveData<Integer> getCountByType(ClothingType type) {
        return clothingDao.getCountByType(type);
    }
    
    public LiveData<Integer> getCountByStatus(ClothingStatus status) {
        return clothingDao.getCountByStatus(status);
    }
    
    public LiveData<Double> getAverageWearCount() {
        return clothingDao.getAverageWearCount();
    }
    
    public LiveData<Double> getTotalValue() {
        return clothingDao.getTotalValue();
    }
    
    // 获取选项数据
    public LiveData<List<String>> getAllColors() {
        return clothingDao.getAllColors();
    }
    
    public LiveData<List<String>> getAllBrands() {
        return clothingDao.getAllBrands();
    }
    
    public LiveData<List<String>> getAllMaterials() {
        return clothingDao.getAllMaterials();
    }
    
    // 异步操作方法
    public CompletableFuture<Long> insertClothing(ClothingItem item) {
        return CompletableFuture.supplyAsync(() -> {
            return clothingDao.insert(item);
        }, WardrobeDatabase.databaseWriteExecutor);
    }
    
    public CompletableFuture<Void> updateClothing(ClothingItem item) {
        return CompletableFuture.runAsync(() -> {
            clothingDao.update(item);
        }, WardrobeDatabase.databaseWriteExecutor);
    }
    
    public CompletableFuture<Void> deleteClothing(ClothingItem item) {
        return CompletableFuture.runAsync(() -> {
            clothingDao.delete(item);
        }, WardrobeDatabase.databaseWriteExecutor);
    }
    
    public CompletableFuture<Void> deleteClothingById(long id) {
        return CompletableFuture.runAsync(() -> {
            clothingDao.deleteById(id);
        }, WardrobeDatabase.databaseWriteExecutor);
    }
    
    public CompletableFuture<Void> incrementWearCount(long id) {
        return CompletableFuture.runAsync(() -> {
            clothingDao.incrementWearCount(id, new Date());
        }, WardrobeDatabase.databaseWriteExecutor);
    }
    
    public CompletableFuture<Void> updateFavoriteStatus(long id, boolean isFavorite) {
        return CompletableFuture.runAsync(() -> {
            clothingDao.updateFavoriteStatus(id, isFavorite);
        }, WardrobeDatabase.databaseWriteExecutor);
    }
    
    public CompletableFuture<Void> updateStatus(long id, ClothingStatus status) {
        return CompletableFuture.runAsync(() -> {
            clothingDao.updateStatus(id, status);
        }, WardrobeDatabase.databaseWriteExecutor);
    }
    
    public CompletableFuture<Void> updateRating(long id, int rating) {
        return CompletableFuture.runAsync(() -> {
            clothingDao.updateRating(id, rating);
        }, WardrobeDatabase.databaseWriteExecutor);
    }
}
