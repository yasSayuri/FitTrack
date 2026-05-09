package com.fittrack;

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
            String idade = edtIdade.getText().toString();
            String peso = edtPeso.getText().toString();
            String altura = edtAltura.getText().toString();

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(Onboarding.this);
                User user = db.userDao().getLastUser();

                if (user != null) {
                    user.idade = idade;
                    user.peso = peso;
                    user.altura = altura;
                    db.userDao().update(user);
                }

                runOnUiThread(() -> {
                    Toast.makeText(Onboarding.this, "Treino cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }
}