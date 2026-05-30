package com.fittrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.TreinoPlano;

public class CriarTreino extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.criar_treino);

        EditText edtTipo = findViewById(R.id.edtTipoRotina);
        EditText edtExercicios = findViewById(R.id.edtExerciciosRotina);
        EditText edtSeries = findViewById(R.id.edtSeriesRotina);
        EditText edtRepeticoes = findViewById(R.id.edtRepeticoesRotina);
        Button btnSalvar = findViewById(R.id.btnSalvarCriarTreino);
        Button btnCancelar = findViewById(R.id.btnCancelarCriarTreino);

        btnCancelar.setOnClickListener(v -> finish());

        btnSalvar.setOnClickListener(v -> {
            String tipo = edtTipo.getText().toString().trim();
            String exercicios = edtExercicios.getText().toString().trim();
            String series = edtSeries.getText().toString().trim();
            String repeticoes = edtRepeticoes.getText().toString().trim();

            if (tipo.isEmpty() || exercicios.isEmpty() || series.isEmpty() || repeticoes.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!series.matches("\\d+") || !repeticoes.matches("\\d+")) {
                Toast.makeText(this, "Séries e repetições devem ser apenas números!", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            int userIdLogado = prefs.getInt("userId", -1);

            if (userIdLogado == -1) {
                Toast.makeText(this, "Erro de sessão!", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(CriarTreino.this);
                TreinoPlano plano = new TreinoPlano();
                plano.userId = userIdLogado;
                plano.tipo = tipo;
                plano.exercicios = exercicios;
                plano.series = series;
                plano.repeticoes = repeticoes;

                db.treinoPlanoDao().insert(plano);

                runOnUiThread(() -> {
                    Toast.makeText(CriarTreino.this, "Rotina criada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }
}