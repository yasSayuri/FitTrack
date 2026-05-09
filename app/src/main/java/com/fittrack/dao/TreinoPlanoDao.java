package com.fittrack.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.fittrack.entidades.TreinoPlano;
import java.util.List;

@Dao
public interface TreinoPlanoDao {
    @Insert
    void insert(TreinoPlano treinoPlano);

    @Query("SELECT * FROM treinos_planos ORDER BY id DESC")
    List<TreinoPlano> getAllTreinosPlanos();
}