package com.fittrack.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.fittrack.entidades.Treino;
import java.util.List;

@Dao
public interface TreinoDao {
    @Insert
    void insert(Treino treino);

    @Query("SELECT * FROM treinos ORDER BY id DESC")
    List<Treino> getAllTreinos();
}