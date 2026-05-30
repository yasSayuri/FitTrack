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
import com.fittrack.entidades.Treino;

public class DetalheTreino extends AppCompatActivity {

    private int idTreino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhe_treino);

        idTreino = getIntent().getIntExtra("id", 0);
        String tipo = getIntent().getStringExtra("tipo");
        String data = getIntent().getStringExtra("data");
        String duracao = getIntent().getStringExtra("duracao");
        String calorias = getIntent().getStringExtra("calorias");
        String desc = getIntent().getStringExtra("descricao");

        TextView txtTipo = findViewById(R.id.txtTipoDetalhe);
        TextView txtData = findViewById(R.id.txtDataDetalhe);
        TextView txtDuracao = findViewById(R.id.txtDuracaoDetalhe);
        TextView txtCalorias = findViewById(R.id.txtCaloriasDetalhe);
        TextView txtDescricao = findViewById(R.id.txtDescricaoDetalhe);
        ImageView btnVoltar = findViewById(R.id.btnVoltarDetalhe);
        Button btnEditar = findViewById(R.id.btnEditar);
        Button btnDeletar = findViewById(R.id.btnDeletar);

        String durFormatada = "0h 0m";
        if (duracao != null && duracao.contains(":")) {
            try {
                String[] p = duracao.split(":");
                durFormatada = p[0] + "h " + p[1] + "m";
            } catch (Exception ignored) {}
        }

        if (txtTipo != null) txtTipo.setText("Tipo: " + (tipo != null ? tipo : "--"));
        if (txtData != null) txtData.setText("Data: " + (data != null ? data : "--"));
        if (txtDuracao != null) txtDuracao.setText("Duração: " + durFormatada);
        if (txtCalorias != null) txtCalorias.setText("Calorias: " + (calorias != null ? calorias : "0") + "k");
        if (txtDescricao != null) txtDescricao.setText("Descrição: " + (desc != null ? desc : "--"));

        if (btnVoltar != null) btnVoltar.setOnClickListener(v -> finish());
        if (btnDeletar != null) btnDeletar.setOnClickListener(v -> deletarTreino());
        if (btnEditar != null) btnEditar.setOnClickListener(v -> mostrarPopUpEdicao(tipo, duracao, calorias, data, desc));
    }

    private void deletarTreino() {
        new Thread(() -> {
            Treino t = new Treino();
            t.id = idTreino;
            AppDatabase.getInstance(this).treinoDao().delete(t);
            runOnUiThread(() -> {
                Toast.makeText(this, "Treino excluído!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    private void mostrarPopUpEdicao(String tipo, String duracao, String calorias, String data, String desc) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_treino);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edtTipo = dialog.findViewById(R.id.edtEditTipo);
        EditText edtDuracao = dialog.findViewById(R.id.edtEditDuracao);
        EditText edtCalorias = dialog.findViewById(R.id.edtEditCalorias);
        EditText edtData = dialog.findViewById(R.id.edtEditData);
        EditText edtDesc = dialog.findViewById(R.id.edtEditDescricao);

        edtTipo.setText(tipo);
        edtDuracao.setText(duracao);
        edtCalorias.setText(calorias);
        edtData.setText(data);
        edtDesc.setText(desc);

        edtDuracao.setFocusable(false);
        edtDuracao.setClickable(true);
        edtDuracao.setOnClickListener(view -> {
            int h = 0, m = 0;
            try {
                if(edtDuracao.getText().toString().contains(":")){
                    String[] p = edtDuracao.getText().toString().split(":");
                    h = Integer.parseInt(p[0]);
                    m = Integer.parseInt(p[1]);
                }
            } catch(Exception ignored){}
            new android.app.TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                edtDuracao.setText(String.format(java.util.Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
            }, h, m, true).show();
        });

        dialog.findViewById(R.id.btnCancelarEditTreino).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.btnSalvarEditTreino).setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            int userIdLogado = prefs.getInt("userId", -1);

            new Thread(() -> {
                Treino t = new Treino();
                t.id = idTreino;
                t.userId = userIdLogado;
                t.tipo = edtTipo.getText().toString().trim();
                t.duracao = edtDuracao.getText().toString().trim();
                t.calorias = edtCalorias.getText().toString().trim();
                t.data = edtData.getText().toString().trim();
                t.descricao = edtDesc.getText().toString().trim();

                AppDatabase.getInstance(this).treinoDao().update(t);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Treino atualizado!", Toast.LENGTH_SHORT).show();
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