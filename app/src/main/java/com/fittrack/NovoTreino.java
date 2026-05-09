package com.fittrack;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.Treino;

public class NovoTreino extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novo_treino);

        EditText edtTipo = findViewById(R.id.edtTipoTreino);
        EditText edtDuracao = findViewById(R.id.edtDuracao);
        EditText edtData = findViewById(R.id.edtDataTreino);
        EditText edtDescricao = findViewById(R.id.edtDescricaoTreino);
        Button btnSalvar = findViewById(R.id.btnSalvarNovoTreino);
        Button btnCancelar = findViewById(R.id.btnCancelarNovoTreino);

        btnCancelar.setOnClickListener(v -> finish());

        btnSalvar.setOnClickListener(v -> {
            String tipo = edtTipo.getText().toString();
            String duracao = edtDuracao.getText().toString();
            String data = edtData.getText().toString();
            String descricao = edtDescricao.getText().toString();

            if (tipo.isEmpty() || duracao.isEmpty()) {
                Toast.makeText(this, "Preencha o tipo e a duração", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(NovoTreino.this);
                Treino treino = new Treino();
                treino.tipo = tipo;
                treino.duracao = duracao;
                treino.data = data;
                treino.descricao = descricao;

                db.treinoDao().insert(treino);

                runOnUiThread(() -> {
                    Toast.makeText(NovoTreino.this, "Treino salvo!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }
}