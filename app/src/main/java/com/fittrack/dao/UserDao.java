package com.fittrack.dao;

import androidx.room.Dao;
import androidx.room.Delete;
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

    @Query("SELECT * FROM usuarios WHERE id = :userId")
    User getUserById(int userId);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}