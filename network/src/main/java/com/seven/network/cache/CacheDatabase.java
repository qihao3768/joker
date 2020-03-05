package com.seven.network.cache;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.seven.common.QiApplication;


@Database(entities = {Cache.class}, version = 1,exportSchema = true)
public abstract class CacheDatabase extends RoomDatabase {
    private static final CacheDatabase database;

    static {
        database = Room.databaseBuilder(QiApplication.getInstance(), CacheDatabase.class, "joker_cache")
                //是否允许在主线程进行查询
                .allowMainThreadQueries()
                //数据库创建和打开后的回调
                //.addCallback()
                //设置查询的线程池
                //.setQueryExecutor()
                //.openHelperFactory()
                //room的日志模式
                //.setJournalMode()
                //数据库升级异常之后的回滚
                //.fallbackToDestructiveMigration()
                //数据库升级异常后根据指定版本进行回滚
                //.fallbackToDestructiveMigrationFrom()
                // .addMigrations(CacheDatabase.sMigration)
                .build();

    }

    public abstract CacheDao getCache();

    public static CacheDatabase get() {
        return database;
    }

//    static Migration sMigration = new Migration(1, 3) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("alter table teacher rename to student");
//            database.execSQL("alter table teacher add column teacher_age INTEGER NOT NULL default 0");
//        }
//    };
}
