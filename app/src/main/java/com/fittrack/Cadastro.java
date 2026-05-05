package com.fittrack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.User;

public class Cadastro extends AppCompatActivity {

    private EditText edtName, edtEmail, edtCpf, edtBirthday, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);

        edtName = findViewById(R.id.edtNameSignup);
        edtEmail = findViewById(R.id.edtEmailSignup);
        edtCpf = findViewById(R.id.edtCpfSignup);
        edtBirthday = findViewById(R.id.edtBirthdaySignup);
        edtPassword = findViewById(R.id.edtPasswordSignup);

        Button btnDoSignup = findViewById(R.id.btnDoSignup);
        ImageView btnVoltar = findViewById(R.id.btnVoltarCadastro);

        btnDoSignup.setOnClickListener(v -> realizarCadastro());

        btnVoltar.setOnClickListener(v -> finish());
    }
    private void realizarCadastro() {
        String nome = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String cpf = edtCpf.getText().toString().trim();
        String data = edtBirthday.getText().toString().trim();
        String senha = edtPassword.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || cpf.isEmpty() || data.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (senha.length() < 6) {
            Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            User novoUsuario = new User(nome, email, cpf, data, senha);

            db.userDao().register(novoUsuario);

            runOnUiThread(() -> {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}