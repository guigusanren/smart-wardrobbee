package com.example.smartwardrobee.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smartwardrobee.model.*;
import com.example.smartwardrobee.repository.ClothingRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 衣物ViewModel
 * 管理UI相关的数据，处理UI和Repository之间的交互
 */
public class ClothingViewModel extends AndroidViewModel {
    
    private ClothingRepository repository;
    private LiveData<List<ClothingItem>> allClothing;
    
    public ClothingViewModel(@NonNull Application application) {
        super(application);
        repository = new ClothingRepository(application);
        allClothing = repository.getAllClothing();
    }
    
    // 获取所有衣物
    public LiveData<List<ClothingItem>> getAllClothing() {
        return allClothing;
    }
    
    // 根据ID获取衣物
    public LiveData<ClothingItem> getClothingById(long id) {
        return repository.getClothingById(id);
    }
    
    // 搜索衣物
    public LiveData<List<ClothingItem>> searchClothing(String query) {
        return repository.searchClothing(query);
    }
    
    // 按类型筛选
    public LiveData<List<ClothingItem>> getClothingByType(ClothingType type) {
        return repository.getClothingByType(type);
    }
    
    // 按季节筛选
    public LiveData<List<ClothingItem>> getClothingBySeason(Season season) {
        return repository.getClothingBySeason(season);
    }
    
    // 按颜色筛选
    public LiveData<List<ClothingItem>> getClothingByColor(String color) {
        return repository.getClothingByColor(color);
    }
    
    // 按状态筛选
    public LiveData<List<ClothingItem>> getClothingByStatus(ClothingStatus status) {
        return repository.getClothingByStatus(status);
    }
    
    // 获取收藏的衣物
    public LiveData<List<ClothingItem>> getFavoriteClothing() {
        return repository.getFavoriteClothing();
    }
    
    // 获取最近穿过的衣物
    public LiveData<List<ClothingItem>> getRecentlyWornClothing(int limit) {
        return repository.getRecentlyWornClothing(limit);
    }
    
    // 获取最常穿的衣物
    public LiveData<List<ClothingItem>> getMostWornClothing(int limit) {
        return repository.getMostWornClothing(limit);
    }
    
    // 获取很少穿的衣物
    public LiveData<List<ClothingItem>> getLeastWornClothing(int maxWearCount) {
        return repository.getLeastWornClothing(maxWearCount);
    }
    
    // 复杂筛选
    public LiveData<List<ClothingItem>> getFilteredClothing(
            ClothingType type, String color, Season season, 
            ClothingStatus status, Boolean isFavorite) {
        return repository.getFilteredClothing(type, color, season, status, isFavorite);
    }
    
    // 统计数据
    public LiveData<Integer> getTotalCount() {
        return repository.getTotalCount();
    }
    
    public LiveData<Integer> getCountByType(ClothingType type) {
        return repository.getCountByType(type);
    }
    
    public LiveData<Integer> getCountByStatus(ClothingStatus status) {
        return repository.getCountByStatus(status);
    }
    
    public LiveData<Double> getAverageWearCount() {
        return repository.getAverageWearCount();
    }
    
    public LiveData<Double> getTotalValue() {
        return repository.getTotalValue();
    }
    
    // 获取选项数据
    public LiveData<List<String>> getAllColors() {
        return repository.getAllColors();
    }
    
    public LiveData<List<String>> getAllBrands() {
        return repository.getAllBrands();
    }
    
    public LiveData<List<String>> getAllMaterials() {
        return repository.getAllMaterials();
    }
    
    // 数据操作方法
    public CompletableFuture<Long> insert(ClothingItem item) {
        return repository.insertClothing(item);
    }
    
    public CompletableFuture<Void> update(ClothingItem item) {
        return repository.updateClothing(item);
    }
    
    public CompletableFuture<Void> delete(ClothingItem item) {
        return repository.deleteClothing(item);
    }
    
    public CompletableFuture<Void> deleteById(long id) {
        return repository.deleteClothingById(id);
    }
    
    public CompletableFuture<Void> incrementWearCount(long id) {
        return repository.incrementWearCount(id);
    }
    
    public CompletableFuture<Void> updateFavoriteStatus(long id, boolean isFavorite) {
        return repository.updateFavoriteStatus(id, isFavorite);
    }
    
    public CompletableFuture<Void> updateStatus(long id, ClothingStatus status) {
        return repository.updateStatus(id, status);
    }
    
    public CompletableFuture<Void> updateRating(long id, int rating) {
        return repository.updateRating(id, rating);
    }
}
