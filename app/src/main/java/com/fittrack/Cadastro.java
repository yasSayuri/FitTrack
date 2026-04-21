package com.fittrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Cadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);

        ImageView btnVoltar = findViewById(R.id.btnVoltarCadastro);
        Button btnDoSignup = findViewById(R.id.btnDoSignup);
        TextView txtIrParaLogin = findViewById(R.id.txtIrParaLogin);

        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(Cadastro.this, MainPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        btnDoSignup.setOnClickListener(v -> {
            Toast.makeText(this, "Cadastro finalizado!", Toast.LENGTH_SHORT).show();
        });

        txtIrParaLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Cadastro.this, Login.class);
            startActivity(intent);
            finish();
        });
    }
}