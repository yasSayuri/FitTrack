package com.fittrack.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fittrack.entidades.TreinoPlano;
import java.util.List;

@Dao
public interface TreinoPlanoDao {
    @Insert
    void insert(TreinoPlano plano);

    @Update
    void update(TreinoPlano plano);

    @Delete
    void delete(TreinoPlano plano);

    @Query("SELECT * FROM treinos_planos WHERE userId = :userId")
    List<TreinoPlano> getTreinosPlanosByUserId(int userId);
}