package com.seven.joker.cache;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.seven.joker.QiApplication;

@Database(entities = {Cache.class}, version = 1, exportSchema = false)
public abstract class CacheDatabase extends RoomDatabase {
    private static final CacheDatabase database;

    static {
        database = Room.databaseBuilder(QiApplication.getInstance(), CacheDatabase.class, "joker_cache")
                .allowMainThreadQueries()
                .build();

    }

    public abstract CacheDao getCache();

    public static CacheDatabase get() {
        return database;
    }

}
