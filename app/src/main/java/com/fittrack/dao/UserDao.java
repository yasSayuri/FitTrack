package com.fittrack.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fittrack.entidades.User;

@Dao
public interface UserDao {
    @Insert
    void register(User user);

    @Query("SELECT * FROM usuarios WHERE email = :email AND senha = :senha LIMIT 1")
    User login(String email, String senha);

    @Query("SELECT * FROM usuarios ORDER BY id DESC LIMIT 1")
    User getLastUser();

    @Update
    void update(User user);
}