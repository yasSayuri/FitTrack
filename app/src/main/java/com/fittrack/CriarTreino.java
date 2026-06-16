package com.fittrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.ExercicioTreino;
import com.fittrack.entidades.TreinoPlano;
import java.util.ArrayList;
import java.util.List;

public class CriarTreino extends AppCompatActivity {

    private LinearLayout containerExercicios;
    private final List<EditText> listaExercicios = new ArrayList<>();
    private final List<EditText> listaSeries = new ArrayList<>();
    private final List<EditText> listaRepeticoes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.criar_treino);

        EditText edtTipo = findViewById(R.id.edtTipoRotina);
        Button btnSalvar = findViewById(R.id.btnSalvarCriarTreino);
        Button btnCancelar = findViewById(R.id.btnCancelarCriarTreino);
        Button btnAdicionarExercicio = findViewById(R.id.btnAdicionarExercicio);

        containerExercicios = findViewById(R.id.containerExercicios);

        adicionarCampoExercicio();

        btnAdicionarExercicio.setOnClickListener(v -> adicionarCampoExercicio());

        btnCancelar.setOnClickListener(v -> finish());

        btnSalvar.setOnClickListener(v -> {
            String tipo = edtTipo.getText().toString().trim();

            if (tipo.isEmpty()) {
                Toast.makeText(this, "Preencha o nome do treino!", Toast.LENGTH_SHORT).show();
                return;
            }

            List<String> exercicios = new ArrayList<>();
            List<Integer> series = new ArrayList<>();
            List<Integer> repeticoes = new ArrayList<>();

            for (int i = 0; i < listaExercicios.size(); i++) {
                String exercicio = listaExercicios.get(i).getText().toString().trim();
                String serie = listaSeries.get(i).getText().toString().trim();
                String repeticao = listaRepeticoes.get(i).getText().toString().trim();

                if (exercicio.isEmpty() || serie.isEmpty() || repeticao.isEmpty()) {
                    Toast.makeText(this, "Preencha todos os campos dos exercícios!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!serie.matches("\\d+") || !repeticao.matches("\\d+")) {
                    Toast.makeText(this, "Séries e repetições devem ser apenas números!", Toast.LENGTH_SHORT).show();
                    return;
                }

                exercicios.add(exercicio);
                series.add(Integer.parseInt(serie));
                repeticoes.add(Integer.parseInt(repeticao));
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

                long idTreinoCriado = db.treinoPlanoDao().insert(plano);

                for (int i = 0; i < exercicios.size(); i++) {
                    ExercicioTreino exercicioTreino = new ExercicioTreino();
                    exercicioTreino.treinoPlanoId = (int) idTreinoCriado;
                    exercicioTreino.exercicio = exercicios.get(i);
                    exercicioTreino.series = series.get(i);
                    exercicioTreino.repeticoes = repeticoes.get(i);

                    db.exercicioTreinoDao().insert(exercicioTreino);
                }

                runOnUiThread(() -> {
                    Toast.makeText(CriarTreino.this, "Rotina criada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }

    private void adicionarCampoExercicio() {
        LinearLayout bloco = new LinearLayout(this);
        bloco.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        bloco.setOrientation(LinearLayout.VERTICAL);

        EditText edtExercicio = new EditText(this);
        edtExercicio.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(50)
        ));
        edtExercicio.setSingleLine(true);
        edtExercicio.setHint("Exercício (Ex: Agachamento)");
        edtExercicio.setTextColor(ContextCompat.getColor(this, R.color.text_white));
        edtExercicio.setHintTextColor(ContextCompat.getColor(this, R.color.text_hint));
        edtExercicio.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_dark));

        LinearLayout linhaSeriesRepeticoes = new LinearLayout(this);
        linhaSeriesRepeticoes.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        linhaSeriesRepeticoes.setOrientation(LinearLayout.HORIZONTAL);
        linhaSeriesRepeticoes.setPadding(0, dp(8), 0, dp(16));

        EditText edtSeries = new EditText(this);
        LinearLayout.LayoutParams paramsSeries = new LinearLayout.LayoutParams(
                0,
                dp(50),
                1
        );
        paramsSeries.setMargins(0, 0, dp(4), 0);
        edtSeries.setLayoutParams(paramsSeries);
        edtSeries.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtSeries.setSingleLine(true);
        edtSeries.setHint("Séries");
        edtSeries.setTextColor(ContextCompat.getColor(this, R.color.text_white));
        edtSeries.setHintTextColor(ContextCompat.getColor(this, R.color.text_hint));
        edtSeries.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_dark));

        EditText edtRepeticoes = new EditText(this);
        LinearLayout.LayoutParams paramsRepeticoes = new LinearLayout.LayoutParams(
                0,
                dp(50),
                1
        );
        paramsRepeticoes.setMargins(dp(4), 0, 0, 0);
        edtRepeticoes.setLayoutParams(paramsRepeticoes);
        edtRepeticoes.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtRepeticoes.setSingleLine(true);
        edtRepeticoes.setHint("Repetições");
        edtRepeticoes.setTextColor(ContextCompat.getColor(this, R.color.text_white));
        edtRepeticoes.setHintTextColor(ContextCompat.getColor(this, R.color.text_hint));
        edtRepeticoes.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_dark));

        linhaSeriesRepeticoes.addView(edtSeries);
        linhaSeriesRepeticoes.addView(edtRepeticoes);

        bloco.addView(edtExercicio);
        bloco.addView(linhaSeriesRepeticoes);

        containerExercicios.addView(bloco);

        listaExercicios.add(edtExercicio);
        listaSeries.add(edtSeries);
        listaRepeticoes.add(edtRepeticoes);
    }

    private int dp(int valor) {
        return (int) (valor * getResources().getDisplayMetrics().density);
    }
}