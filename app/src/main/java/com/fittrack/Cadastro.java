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
        TextView txtIrParaLogin = findViewById(R.id.txtIrParaLogin);

        configurarMascaraData();

        btnDoSignup.setOnClickListener(v -> realizarCadastro());

        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(Cadastro.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        if (txtIrParaLogin != null) {
            txtIrParaLogin.setOnClickListener(v -> {
                Intent intent = new Intent(Cadastro.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }
    }

    private void configurarMascaraData() {
        edtBirthday.addTextChangedListener(new TextWatcher() {
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
                edtBirthday.setText(formatado.toString());
                edtBirthday.setSelection(formatado.length());
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

            db.userDao().register(novoUsuario);

            runOnUiThread(() -> {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Cadastro.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }).start();
    }
}