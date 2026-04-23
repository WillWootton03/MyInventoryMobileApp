package com.zybooks.cs360finalproject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDAO {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findByUsername(String username);

    @Query("SELECT * FROM users WHERE username = :username and passwordHash = :hash LIMIT 1")
    User login(String username, String hash);

    @Query("SELECT phoneNumber FROM users WHERE userId = :id")
    String getPhoneNumber(String id);
}
