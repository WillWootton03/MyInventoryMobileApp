package com.zybooks.cs360finalproject;

import android.content.Context;
import androidx.room.Room;
public class AppDatabaseClient {
    private  static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {

            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "app_database"
            )
            .allowMainThreadQueries()
            .build();
        }
        return instance;
    }
}
