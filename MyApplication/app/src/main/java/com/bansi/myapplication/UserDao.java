package com.bansi.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<User> users);

    @Query("SELECT * FROM users ORDER BY firstName ASC")
    LiveData<List<User>> getUsersSortedByName();

    @Query("SELECT * FROM users ORDER BY email ASC")
    LiveData<List<User>> getUsersSortedByEmail();

    @Query("SELECT * FROM users ORDER BY lastName ASC")
    LiveData<List<User>> getUsersSortedByLastName();
}
