package com.usermanage.dao.userList;

import android.app.Activity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = UserList.class, version = 1)
public abstract class UserListDatabase extends RoomDatabase {
    public static UserListDatabase instance;
    public static final String DATABASE_NAME = "UserList";

    public abstract DaoQueryList daoQueryList();

    public static UserListDatabase getInsance(Activity context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, UserListDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
