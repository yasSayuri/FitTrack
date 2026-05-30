package com.fittrack.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fittrack.entidades.Treino;
import java.util.List;

@Dao
public interface TreinoDao {
    @Insert
    void insert(Treino treino);

    @Update
    void update(Treino treino);

    @Delete
    void delete(Treino treino);

    @Query("SELECT * FROM treinos ORDER BY id DESC")
    List<Treino> getAllTreinos();

    @Query("SELECT * FROM treinos WHERE userId = :idUsuario")
    List<Treino> getTreinosByUserId(int idUsuario);
}