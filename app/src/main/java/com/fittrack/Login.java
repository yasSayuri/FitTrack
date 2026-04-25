package com.fittrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ImageView btnVoltar = findViewById(R.id.btnVoltarLogin);
        Button btnDoLogin = findViewById(R.id.btnDoLogin);
        TextView txtIrParaCadastro = findViewById(R.id.txtIrParaCadastro);

        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, MainPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        btnDoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
            //finish();
        });

        txtIrParaCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Cadastro.class);
            startActivity(intent);
        });
    }
}