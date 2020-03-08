package com.seven.joker.network;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cache")
public class Cache implements Serializable {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String key;
    public byte[] data;

}
