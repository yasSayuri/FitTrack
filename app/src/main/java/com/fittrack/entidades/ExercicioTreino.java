package com.fittrack.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "exercicios_treino",
        foreignKeys = @ForeignKey(
                entity = TreinoPlano.class,
                parentColumns = "id",
                childColumns = "treinoPlanoId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("treinoPlanoId")}
)
public class ExercicioTreino {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int treinoPlanoId;
    public String exercicio;
    public int series;
    public int repeticoes;
}