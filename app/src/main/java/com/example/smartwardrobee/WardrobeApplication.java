package com.example.smartwardrobee;

import android.app.Application;
import com.example.smartwardrobee.database.WardrobeDatabase;

/**
 * 应用程序类
 * 用于初始化全局组件
 */
public class WardrobeApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // 预初始化数据库
        WardrobeDatabase.getDatabase(this);
    }
    
    @Override
    public void onTerminate() {
        super.onTerminate();
        
        // 关闭数据库连接
        WardrobeDatabase.closeDatabase();
    }
}
