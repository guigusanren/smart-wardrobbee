package com.example.smartwardrobee.model;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * Room数据库类型转换器
 * 用于转换复杂数据类型
 */
public class Converters {
    
    private static final Gson gson = new Gson();
    
    // Date转换
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
    
    // List<String>转换
    @TypeConverter
    public static List<String> fromStringList(String value) {
        if (value == null) {
            return null;
        }
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(value, listType);
    }
    
    @TypeConverter
    public static String fromListString(List<String> list) {
        if (list == null) {
            return null;
        }
        return gson.toJson(list);
    }
    
    // List<Season>转换
    @TypeConverter
    public static List<Season> fromSeasonList(String value) {
        if (value == null) {
            return null;
        }
        Type listType = new TypeToken<List<Season>>() {}.getType();
        return gson.fromJson(value, listType);
    }
    
    @TypeConverter
    public static String fromListSeason(List<Season> list) {
        if (list == null) {
            return null;
        }
        return gson.toJson(list);
    }
    
    // List<Occasion>转换
    @TypeConverter
    public static List<Occasion> fromOccasionList(String value) {
        if (value == null) {
            return null;
        }
        Type listType = new TypeToken<List<Occasion>>() {}.getType();
        return gson.fromJson(value, listType);
    }
    
    @TypeConverter
    public static String fromListOccasion(List<Occasion> list) {
        if (list == null) {
            return null;
        }
        return gson.toJson(list);
    }
    
    // ClothingType转换
    @TypeConverter
    public static ClothingType fromClothingType(String value) {
        return value == null ? null : ClothingType.valueOf(value);
    }
    
    @TypeConverter
    public static String fromClothingType(ClothingType type) {
        return type == null ? null : type.name();
    }
    
    // ClothingStatus转换
    @TypeConverter
    public static ClothingStatus fromClothingStatus(String value) {
        return value == null ? null : ClothingStatus.valueOf(value);
    }
    
    @TypeConverter
    public static String fromClothingStatus(ClothingStatus status) {
        return status == null ? null : status.name();
    }
}
