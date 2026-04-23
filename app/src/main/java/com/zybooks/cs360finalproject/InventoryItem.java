package com.zybooks.cs360finalproject;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.UUID;


    @Entity(
            tableName = "inventory",
            foreignKeys = @ForeignKey(
                    entity = User.class,
                    parentColumns = "userId",
                    childColumns = "userOwnerId",
                    onDelete = CASCADE
            )
    )
    public class InventoryItem {

        @NonNull
        @PrimaryKey
        public String id; // autogenerate UUID

        public String itemName;
        public int itemQuantity;

        public String userOwnerId; // foreign key reference to users table

        public InventoryItem() {
            this.id = UUID.randomUUID().toString();
        }
    }
