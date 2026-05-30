package com.fittrack;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.TreinoPlano;

public class DetalheTreinoPlano extends AppCompatActivity {

    private int idPlano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhe_treino_plano);

        idPlano = getIntent().getIntExtra("id", 0);
        String tipo = getIntent().getStringExtra("tipo");
        String exercicios = getIntent().getStringExtra("exercicios");
        String series = getIntent().getStringExtra("series");
        String repeticoes = getIntent().getStringExtra("repeticoes");

        TextView txtTipo = findViewById(R.id.txtTipoDetalhePlano);
        TextView txtSeries = findViewById(R.id.txtSeriesDetalhePlano);
        TextView txtRepeticoes = findViewById(R.id.txtRepeticoesDetalhePlano);
        TextView txtExercicios = findViewById(R.id.txtExerciciosDetalhePlano);
        ImageView btnVoltar = findViewById(R.id.btnVoltarDetalhePlano);
        Button btnEditar = findViewById(R.id.btnEditarPlano);
        Button btnDeletar = findViewById(R.id.btnDeletarPlano);

        if (txtTipo != null) txtTipo.setText(tipo != null ? tipo : "--");
        if (txtSeries != null) txtSeries.setText(series != null ? series : "0");
        if (txtRepeticoes != null) txtRepeticoes.setText(repeticoes != null ? repeticoes : "0");
        if (txtExercicios != null) txtExercicios.setText(exercicios != null ? exercicios : "--");

        if (btnVoltar != null) {
            btnVoltar.setOnClickListener(v -> finish());
        }

        if (btnDeletar != null) {
            btnDeletar.setOnClickListener(v -> deletarPlano());
        }

        if (btnEditar != null) {
            btnEditar.setOnClickListener(v -> mostrarPopUpEdicao(tipo, exercicios, series, repeticoes));
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

    private void mostrarPopUpEdicao(String tipo, String exercicios, String series, String repeticoes) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_treino_plano);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edtTipo = dialog.findViewById(R.id.edtEditTipoPlano);
        EditText edtExercicios = dialog.findViewById(R.id.edtEditExerciciosPlano);
        EditText edtSeries = dialog.findViewById(R.id.edtEditSeriesPlano);
        EditText edtRepeticoes = dialog.findViewById(R.id.edtEditRepeticoesPlano);

        edtTipo.setText(tipo);
        edtExercicios.setText(exercicios);
        edtSeries.setText(series);
        edtRepeticoes.setText(repeticoes);

        dialog.findViewById(R.id.btnCancelarEditPlano).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.btnSalvarEditPlano).setOnClickListener(v -> {
            String novoTipo = edtTipo.getText().toString().trim();
            String novoExc = edtExercicios.getText().toString().trim();
            String novaSerie = edtSeries.getText().toString().trim();
            String novaRep = edtRepeticoes.getText().toString().trim();

            if (novoTipo.isEmpty() || novoExc.isEmpty() || novaSerie.isEmpty() || novaRep.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            int userIdLogado = prefs.getInt("userId", -1);

            new Thread(() -> {
                TreinoPlano p = new TreinoPlano();
                p.id = idPlano;
                p.userId = userIdLogado;
                p.tipo = novoTipo;
                p.exercicios = novoExc;
                p.series = novaSerie;
                p.repeticoes = novaRep;

                AppDatabase.getInstance(this).treinoPlanoDao().update(p);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Rotina atualizada!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                });
            }).start();
        });

        dialog.show();

        if (dialog.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }
}