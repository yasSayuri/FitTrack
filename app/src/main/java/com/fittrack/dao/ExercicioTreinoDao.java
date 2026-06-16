package com.fittrack.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.fittrack.entidades.ExercicioTreino;
import java.util.List;

@Dao
public interface ExercicioTreinoDao {
    @Insert
    void insert(ExercicioTreino exercicioTreino);

    @Query("SELECT * FROM exercicios_treino WHERE treinoPlanoId = :treinoPlanoId")
    List<ExercicioTreino> getExerciciosByTreinoPlanoId(int treinoPlanoId);

    @Query("DELETE FROM exercicios_treino WHERE treinoPlanoId = :treinoPlanoId")
    void deleteByTreinoPlanoId(int treinoPlanoId);
}