package com.zybooks.cs360finalproject;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, InventoryItem.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDAO();
    public abstract InventoryDAO inventoryDAO();
}
