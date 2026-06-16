package com.fittrack;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.ExercicioTreino;
import com.fittrack.entidades.TreinoPlano;

import java.util.ArrayList;
import java.util.List;

public class DetalheTreinoPlano extends AppCompatActivity {

    private int idPlano;
    private String tipoAtual = "";
    private List<ExercicioTreino> exerciciosAtuais = new ArrayList<>();

    private TextView txtTipo;
    private LinearLayout containerExercicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhe_treino_plano);

        idPlano = getIntent().getIntExtra("id", 0);

        txtTipo = findViewById(R.id.txtTipoDetalhePlano);
        containerExercicios = findViewById(R.id.containerExerciciosDetalhePlano);

        ImageView btnVoltar = findViewById(R.id.btnVoltarDetalhePlano);
        Button btnEditar = findViewById(R.id.btnEditarPlano);
        Button btnDeletar = findViewById(R.id.btnDeletarPlano);

        if (btnVoltar != null) {
            btnVoltar.setOnClickListener(v -> finish());
        }

        if (btnDeletar != null) {
            btnDeletar.setOnClickListener(v -> deletarPlano());
        }

        if (btnEditar != null) {
            btnEditar.setOnClickListener(v -> mostrarPopUpEdicao());
        }

        carregarDados();
    }

    private void carregarDados() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            TreinoPlano plano = db.treinoPlanoDao().getTreinoPlanoById(idPlano);
            List<ExercicioTreino> exercicios = db.exercicioTreinoDao().getExerciciosByTreinoPlanoId(idPlano);

            runOnUiThread(() -> {
                if (plano == null) {
                    Toast.makeText(this, "Treino não encontrado!", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                tipoAtual = plano.tipo;
                exerciciosAtuais = exercicios;

                if (txtTipo != null) {
                    txtTipo.setText(plano.tipo != null ? plano.tipo : "--");
                }

                montarListaExercicios(exercicios);
            });
        }).start();
    }

    private void montarListaExercicios(List<ExercicioTreino> exercicios) {
        if (containerExercicios == null) return;

        containerExercicios.removeAllViews();

        if (exercicios == null || exercicios.isEmpty()) {
            TextView vazio = new TextView(this);
            vazio.setText("Nenhum exercício cadastrado.");
            vazio.setTextColor(getResources().getColor(R.color.text_hint));
            vazio.setTextSize(14);
            containerExercicios.addView(vazio);
            return;
        }

        for (int i = 0; i < exercicios.size(); i++) {
            ExercicioTreino e = exercicios.get(i);

            LinearLayout blocoExercicio = new LinearLayout(this);
            blocoExercicio.setOrientation(LinearLayout.VERTICAL);
            blocoExercicio.setPadding(0, 4, 0, 4);

            TextView txtNomeExercicio = new TextView(this);
            txtNomeExercicio.setText(e.exercicio != null ? e.exercicio : "--");
            txtNomeExercicio.setTextColor(getResources().getColor(R.color.text_white));
            txtNomeExercicio.setTextSize(16);
            txtNomeExercicio.setTypeface(null, Typeface.BOLD);

            LinearLayout linhaSeriesRepeticoes = new LinearLayout(this);
            linhaSeriesRepeticoes.setOrientation(LinearLayout.HORIZONTAL);
            linhaSeriesRepeticoes.setPadding(0, 8, 0, 0);

            TextView txtSerie = new TextView(this);
            txtSerie.setText("Séries: " + e.series);
            txtSerie.setTextColor(getResources().getColor(R.color.text_hint));
            txtSerie.setTextSize(14);

            LinearLayout.LayoutParams paramsSerie = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            );

            txtSerie.setLayoutParams(paramsSerie);

            TextView txtRepeticao = new TextView(this);
            txtRepeticao.setText("Repetições: " + e.repeticoes);
            txtRepeticao.setTextColor(getResources().getColor(R.color.text_hint));
            txtRepeticao.setTextSize(14);

            LinearLayout.LayoutParams paramsRepeticao = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            );

            txtRepeticao.setLayoutParams(paramsRepeticao);

            linhaSeriesRepeticoes.addView(txtSerie);
            linhaSeriesRepeticoes.addView(txtRepeticao);

            blocoExercicio.addView(txtNomeExercicio);
            blocoExercicio.addView(linhaSeriesRepeticoes);

            containerExercicios.addView(blocoExercicio);

            if (i < exercicios.size() - 1) {
                View linhaRoxa = new View(this);

                LinearLayout.LayoutParams paramsLinha = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2
                );

                paramsLinha.setMargins(0, 14, 0, 14);

                linhaRoxa.setLayoutParams(paramsLinha);
                linhaRoxa.setBackgroundColor(getResources().getColor(R.color.purple_primary));

                containerExercicios.addView(linhaRoxa);
            }
        }
    }

    private void deletarPlano() {
        new Thread(() -> {
            TreinoPlano p = new TreinoPlano();
            p.id = idPlano;

            AppDatabase.getInstance(this).treinoPlanoDao().delete(p);

            runOnUiThread(() -> {
                Toast.makeText(this, "Rotina excluída!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    private void mostrarPopUpEdicao() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_treino_plano);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.setCancelable(false);

        EditText edtTipo = dialog.findViewById(R.id.edtEditTipoPlano);
        LinearLayout containerEditExercicios = dialog.findViewById(R.id.containerEditExerciciosPlano);
        Button btnAdicionarExercicio = dialog.findViewById(R.id.btnAdicionarEditExercicio);

        edtTipo.setText(tipoAtual);

        containerEditExercicios.removeAllViews();

        for (ExercicioTreino e : exerciciosAtuais) {
            adicionarBlocoEdicaoExercicio(
                    containerEditExercicios,
                    e.exercicio,
                    String.valueOf(e.series),
                    String.valueOf(e.repeticoes)
            );
        }

        if (exerciciosAtuais.isEmpty()) {
            adicionarBlocoEdicaoExercicio(containerEditExercicios, "", "", "");
        }

        btnAdicionarExercicio.setOnClickListener(v -> {
            adicionarBlocoEdicaoExercicio(containerEditExercicios, "", "", "");
        });

        dialog.findViewById(R.id.btnCancelarEditPlano).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.btnSalvarEditPlano).setOnClickListener(v -> {
            String novoTipo = edtTipo.getText().toString().trim();

            if (novoTipo.isEmpty()) {
                Toast.makeText(this, "Informe o tipo do treino!", Toast.LENGTH_SHORT).show();
                return;
            }

            List<ExercicioTreino> novosExercicios = new ArrayList<>();

            for (int i = 0; i < containerEditExercicios.getChildCount(); i++) {
                View view = containerEditExercicios.getChildAt(i);

                if (!(view instanceof LinearLayout)) continue;

                Object tag = view.getTag();

                if (tag == null || !tag.equals("bloco_exercicio")) continue;

                LinearLayout bloco = (LinearLayout) view;

                EditText edtExercicio = (EditText) bloco.getChildAt(0);
                LinearLayout linhaInfo = (LinearLayout) bloco.getChildAt(1);

                EditText edtSerie = (EditText) linhaInfo.getChildAt(0);
                EditText edtRepeticao = (EditText) linhaInfo.getChildAt(1);

                String exercicio = edtExercicio.getText().toString().trim();
                String serie = edtSerie.getText().toString().trim();
                String repeticao = edtRepeticao.getText().toString().trim();

                if (exercicio.isEmpty() || serie.isEmpty() || repeticao.isEmpty()) {
                    Toast.makeText(this, "Preencha todos os exercícios!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!serie.matches("\\d+") || !repeticao.matches("\\d+")) {
                    Toast.makeText(this, "Séries e repetições devem ser números!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ExercicioTreino e = new ExercicioTreino();
                e.treinoPlanoId = idPlano;
                e.exercicio = exercicio;
                e.series = Integer.parseInt(serie);
                e.repeticoes = Integer.parseInt(repeticao);

                novosExercicios.add(e);
            }

            if (novosExercicios.isEmpty()) {
                Toast.makeText(this, "Adicione pelo menos um exercício!", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            int userIdLogado = prefs.getInt("userId", -1);

            if (userIdLogado == -1) {
                Toast.makeText(this, "Erro de sessão!", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(this);

                TreinoPlano p = new TreinoPlano();
                p.id = idPlano;
                p.userId = userIdLogado;
                p.tipo = novoTipo;

                db.treinoPlanoDao().update(p);
                db.exercicioTreinoDao().deleteByTreinoPlanoId(idPlano);

                for (ExercicioTreino e : novosExercicios) {
                    db.exercicioTreinoDao().insert(e);
                }

                runOnUiThread(() -> {
                    Toast.makeText(this, "Rotina atualizada!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    carregarDados();
                });
            }).start();
        });

        dialog.show();

        if (dialog.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }
    private void adicionarBlocoEdicaoExercicio(
            LinearLayout container,
            String nomeExercicio,
            String series,
            String repeticoes
    ) {
        LinearLayout bloco = new LinearLayout(this);
        bloco.setOrientation(LinearLayout.VERTICAL);
        bloco.setPadding(0, 8, 0, 8);
        bloco.setTag("bloco_exercicio");

        EditText edtExercicio = new EditText(this);
        edtExercicio.setText(nomeExercicio);
        edtExercicio.setHint("Nome do exercício");
        edtExercicio.setSingleLine(true);
        edtExercicio.setTextColor(getResources().getColor(R.color.text_white));
        edtExercicio.setHintTextColor(getResources().getColor(R.color.text_hint));
        edtExercicio.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                getResources().getColor(R.color.purple_dark)
        ));

        LinearLayout linhaInfo = new LinearLayout(this);
        linhaInfo.setOrientation(LinearLayout.HORIZONTAL);
        linhaInfo.setPadding(0, 8, 0, 0);

        EditText edtSeries = new EditText(this);
        edtSeries.setText(series);
        edtSeries.setHint("Séries");
        edtSeries.setSingleLine(true);
        edtSeries.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtSeries.setTextColor(getResources().getColor(R.color.text_white));
        edtSeries.setHintTextColor(getResources().getColor(R.color.text_hint));
        edtSeries.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                getResources().getColor(R.color.purple_dark)
        ));

        LinearLayout.LayoutParams paramsSeries = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );

        paramsSeries.setMargins(0, 0, 8, 0);
        edtSeries.setLayoutParams(paramsSeries);

        EditText edtRepeticoes = new EditText(this);
        edtRepeticoes.setText(repeticoes);
        edtRepeticoes.setHint("Repetições");
        edtRepeticoes.setSingleLine(true);
        edtRepeticoes.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtRepeticoes.setTextColor(getResources().getColor(R.color.text_white));
        edtRepeticoes.setHintTextColor(getResources().getColor(R.color.text_hint));
        edtRepeticoes.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                getResources().getColor(R.color.purple_dark)
        ));

        LinearLayout.LayoutParams paramsRepeticoes = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );

        paramsRepeticoes.setMargins(8, 0, 0, 0);
        edtRepeticoes.setLayoutParams(paramsRepeticoes);

        linhaInfo.addView(edtSeries);
        linhaInfo.addView(edtRepeticoes);

        bloco.addView(edtExercicio);
        bloco.addView(linhaInfo);

        container.addView(bloco);

        View linhaRoxa = new View(this);

        LinearLayout.LayoutParams paramsLinha = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
        );

        paramsLinha.setMargins(0, 12, 0, 12);

        linhaRoxa.setLayoutParams(paramsLinha);
        linhaRoxa.setBackgroundColor(getResources().getColor(R.color.purple_primary));

        container.addView(linhaRoxa);
    }
}