package com.usermanage.dao.user;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoQuery {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(User... users);

    @Query("DELETE FROM User WHERE id = :id")
    void deleteUserById(int id);

    @Update
    void update(User... users);

    @Query("UPDATE User SET Avatar =:avatar Where id=1")
    void updateAvatar(String avatar);

    @Query("SELECT * FROM User")
    User getUser();

    @Query("SELECT * FROM User")
    List<User> getAll();
}
