package com.fittrack.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "treinos")
public class Treino {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String tipo;
    public String duracao;
    public String data;
    public String descricao;
    public String calorias;
}