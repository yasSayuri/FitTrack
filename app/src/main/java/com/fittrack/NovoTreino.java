package com.fittrack;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.Treino;

public class NovoTreino extends AppCompatActivity {

    private EditText edtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novo_treino);

        EditText edtTipo = findViewById(R.id.edtTipoTreino);
        EditText edtDuracao = findViewById(R.id.edtDuracao);
        EditText edtCalorias = findViewById(R.id.edtCaloriasTreino);
        edtData = findViewById(R.id.edtDataTreino);
        EditText edtDescricao = findViewById(R.id.edtDescricaoTreino);
        Button btnSalvar = findViewById(R.id.btnSalvarNovoTreino);
        Button btnCancelar = findViewById(R.id.btnCancelarNovoTreino);

        configurarMascaraData();

        btnCancelar.setOnClickListener(v -> finish());

        btnSalvar.setOnClickListener(v -> {
            String tipo = edtTipo.getText().toString().trim();
            String duracao = edtDuracao.getText().toString().trim();
            String calorias = edtCalorias.getText().toString().trim();
            String data = edtData.getText().toString().trim();
            String descricao = edtDescricao.getText().toString().trim();

            if (tipo.isEmpty() || duracao.isEmpty() || data.isEmpty()) {
                Toast.makeText(this, "Preencha o tipo, duração e data", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            int userIdLogado = prefs.getInt("userId", -1);

            if (userIdLogado == -1) {
                Toast.makeText(this, "Erro de autenticação! Faça login novamente.", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                try {
                    AppDatabase db = AppDatabase.getInstance(NovoTreino.this);
                    Treino treino = new Treino();
                    treino.userId = userIdLogado;
                    treino.tipo = tipo;
                    treino.duracao = duracao;
                    treino.calorias = calorias;
                    treino.data = data;
                    treino.descricao = descricao;

                    db.treinoDao().insert(treino);

                    runOnUiThread(() -> {
                        Toast.makeText(NovoTreino.this, "Treino salvo!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(NovoTreino.this, "Erro ao salvar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }).start();
        });
    }

    private void configurarMascaraData() {
        edtData.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String str = s.toString().replaceAll("[^0-9]", "");
                StringBuilder formatado = new StringBuilder();

                if (str.length() >= 2) {
                    formatado.append(str.substring(0, 2));
                    if (str.length() > 2) formatado.append("/");
                } else {
                    formatado.append(str);
                }

                if (str.length() >= 4) {
                    formatado.append(str.substring(2, 4));
                    if (str.length() > 4) formatado.append("/");
                } else if (str.length() > 2) {
                    formatado.append(str.substring(2));
                }

                if (str.length() > 4) {
                    if (str.length() > 8) {
                        formatado.append(str.substring(4, 8));
                    } else {
                        formatado.append(str.substring(4));
                    }
                }

                isUpdating = true;
                edtData.setText(formatado.toString());
                edtData.setSelection(formatado.length());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}