package com.seven.network.cache;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Cache cache);


    //如果是一对多,这里可以写List<Cache>
    @Query("select *from cache where `key`=:key")
    Cache getCache(String key);

    //只能传递对象昂,删除时根据Cache中的主键 来比对的
    @Delete
    int delete(Cache cache);

    //只能传递对象昂,删除时根据Cache中的主键 来比对的
    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Cache cache);
}
