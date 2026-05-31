package com.fittrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.User;

public class Onboarding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding);

        EditText edtIdade = findViewById(R.id.edtIdade);
        EditText edtPeso = findViewById(R.id.edtPeso);
        EditText edtAltura = findViewById(R.id.edtAltura);
        Button btnSalvarTreino = findViewById(R.id.btnSalvarTreino);

        btnSalvarTreino.setOnClickListener(v -> {
            String idade = edtIdade.getText().toString().trim();
            String peso = edtPeso.getText().toString().trim();
            String altura = edtAltura.getText().toString().trim();

            if (idade.isEmpty() || peso.isEmpty() || altura.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            int userIdLogado = prefs.getInt("userId", -1);

            if (userIdLogado == -1) {
                Toast.makeText(this, "Erro de sessão!", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(Onboarding.this);
                User user = db.userDao().getUserById(userIdLogado);

                if (user != null) {
                    user.idade = idade;
                    user.peso = peso;
                    user.altura = altura;
                    db.userDao().update(user);
                }

                runOnUiThread(() -> {
                    Toast.makeText(Onboarding.this, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }
}