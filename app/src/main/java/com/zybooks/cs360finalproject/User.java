package com.zybooks.cs360finalproject;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "users")
public class User {

    @NonNull
    @PrimaryKey
    public String userId;

    public String username;
    public String passwordHash; // stored hash of the password
    public String salt; // unique salt key stored with password
    public String phoneNumber;

    // Auto-generates when creating new user
    public User() {
        this.userId = UUID.randomUUID().toString();
    }

}
