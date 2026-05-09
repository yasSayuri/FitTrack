package com.fittrack.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "treinos_planos")
public class TreinoPlano {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String tipo;
    public String exercicios;
    public String series;
    public String repeticoes;
}