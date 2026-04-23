package com.zybooks.cs360finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface InventoryDAO {

    @Insert
    void insert(InventoryItem item);

    @Delete
    void delete(InventoryItem item);

    @Update
    void update(InventoryItem item);

    @Query("DELETE FROM inventory WHERE id = :itemId")
    void deleteById(String itemId);

    @Query("SELECT * FROM inventory WHERE userOwnerId = :userId")
    List<InventoryItem> getItemsForUser(String userId);

    @Query("SELECT * FROM inventory WHERE userOwnerId = :userId AND id = :itemId")
    InventoryItem getItem(String userId, String itemId);
}
