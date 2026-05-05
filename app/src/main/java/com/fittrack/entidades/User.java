package com.fittrack.entidades;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nome;
    public String email;
    public String cpf;
    public String dataNascimento;
    public String senha;

    public User() {}

    @Ignore
    public User(String nome, String email, String cpf, String dataNascimento, String senha) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.senha = senha;
    }
}