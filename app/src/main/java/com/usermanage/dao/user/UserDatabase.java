package com.usermanage.dao.user;

import android.app.Activity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = User.class, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public static UserDatabase instance;
    public static final String DATABASE_NAME = "User";

    public abstract DaoQuery daoQuery();

    public static UserDatabase getInsance(Activity context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, UserDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
