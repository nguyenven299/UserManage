package com.usermanage.dao.userList;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoQueryList {
    @Insert
    void insertData(UserList... users);

    @Query("DELETE From UserList")
    void deleteUserListById();

    @Update
    void update(UserList... userLists);

    @Query("SELECT * FROM UserList")
    UserList getUser();

    @Query("SELECT * FROM UserList")
    List<UserList> getAll();
}
