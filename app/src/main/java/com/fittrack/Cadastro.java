package com.fittrack;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

    private EditText edtName, edtUsername, edtEmail, edtCpf, edtBirthday, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);

        edtName = findViewById(R.id.edtNameSignup);
        edtUsername = findViewById(R.id.edtUsernameSignup);
        edtEmail = findViewById(R.id.edtEmailSignup);
        edtCpf = findViewById(R.id.edtCpfSignup);
        edtBirthday = findViewById(R.id.edtBirthdaySignup);
        edtPassword = findViewById(R.id.edtPasswordSignup);

        Button btnDoSignup = findViewById(R.id.btnDoSignup);
        ImageView btnVoltar = findViewById(R.id.btnVoltarCadastro);

        configurarMascaraData();

        btnDoSignup.setOnClickListener(v -> realizarCadastro());

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void configurarMascaraData() {
        edtBirthday.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private String oldString = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString().replaceAll("[^\\d]", "");
                String mask = "##/##/####";
                String novaString = "";

                if (isUpdating) {
                    oldString = str;
                    isUpdating = false;
                    return;
                }

                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > oldString.length()) {
                        novaString += m;
                        continue;
                    }
                    try {
                        novaString += str.charAt(i);
                        i++;
                    } catch (Exception e) {
                        break;
                    }
                }

                isUpdating = true;
                edtBirthday.setText(novaString);
                edtBirthday.setSelection(novaString.length());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void realizarCadastro() {
        String nome = edtName.getText().toString().trim();
        String usuario = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String cpf = edtCpf.getText().toString().trim();
        String data = edtBirthday.getText().toString().trim();
        String senha = edtPassword.getText().toString().trim();

        if (nome.isEmpty() || usuario.isEmpty() || email.isEmpty() || cpf.isEmpty() || data.isEmpty() || senha.isEmpty()) {
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
            User novoUsuario = new User(nome, usuario, email, cpf, data, senha);
            novoUsuario.usuario = usuario;

            db.userDao().register(novoUsuario);

            runOnUiThread(() -> {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}