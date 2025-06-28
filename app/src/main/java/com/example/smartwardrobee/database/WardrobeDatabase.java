package com.example.smartwardrobee.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.smartwardrobee.model.ClothingItem;
import com.example.smartwardrobee.model.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 智能衣柜数据库
 * 使用Room数据库框架
 */
@Database(
    entities = {ClothingItem.class},
    version = 1,
    exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class WardrobeDatabase extends RoomDatabase {
    
    // 数据库名称
    private static final String DATABASE_NAME = "wardrobe_database";
    
    // 单例实例
    private static volatile WardrobeDatabase INSTANCE;
    
    // 线程池，用于数据库操作
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = 
        Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    
    // 抽象方法，返回DAO
    public abstract ClothingDao clothingDao();
    
    /**
     * 获取数据库实例（单例模式）
     */
    public static WardrobeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WardrobeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        WardrobeDatabase.class,
                        DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration() // 开发阶段使用，生产环境需要提供迁移策略
                    .build();
                }
            }
        }
        return INSTANCE;
    }
    
    /**
     * 关闭数据库连接
     */
    public static void closeDatabase() {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}
