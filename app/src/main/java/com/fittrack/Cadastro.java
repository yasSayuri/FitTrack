package com.fittrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.User;
import com.fittrack.utils.CpfValidator;

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

        configurarMascaraData();
        configurarMascaraCpf();

        findViewById(R.id.btnDoSignup).setOnClickListener(v -> realizarCadastro());
        findViewById(R.id.btnVoltarCadastro).setOnClickListener(v -> finish());

        TextView txtIrParaLogin = findViewById(R.id.txtIrParaLogin);
        if (txtIrParaLogin != null) {
            txtIrParaLogin.setOnClickListener(v -> finish());
        }
    }

    private void realizarCadastro() {
        String nome = edtName.getText().toString().trim();
        String user = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String cpf = edtCpf.getText().toString().trim();
        String data = edtBirthday.getText().toString().trim();
        String senha = edtPassword.getText().toString().trim();

        if (nome.isEmpty() || user.isEmpty() || email.isEmpty() || cpf.isEmpty() || data.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!CpfValidator.isCpfValido(cpf)) {
            Toast.makeText(this, "CPF inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (senha.length() < 6) {
            Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            // Validação de e-mail e CPF únicos
            if (db.userDao().getUserByEmail(email) != null) {
                runOnUiThread(() -> Toast.makeText(this, "Este e-mail já está cadastrado!", Toast.LENGTH_SHORT).show());
                return;
            }

            if (db.userDao().getUserByCpf(cpf) != null) {
                runOnUiThread(() -> Toast.makeText(this, "Este CPF já possui cadastro!", Toast.LENGTH_SHORT).show());
                return;
            }

            User novoUsuario = new User(nome, user, email, cpf, data, senha);
            db.userDao().register(novoUsuario);

            User registrado = db.userDao().login(email, senha);
            if (registrado != null) {
                getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().putInt("userId", registrado.id).apply();
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Cadastro.this, Onboarding.class));
                finish();
            });
        }).start();
    }
    private void configurarMascaraData() {
        edtBirthday.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) { isUpdating = false; return; }

                String str = s.toString().replaceAll("[^0-9]", "");
                StringBuilder formatado = new StringBuilder();

                int len = str.length();
                if (len > 0) {
                    if (len <= 2) {
                        formatado.append(str);
                    } else if (len <= 4) {
                        formatado.append(str.substring(0, 2)).append("/").append(str.substring(2));
                    } else {
                        // Limita a 8 dígitos para evitar erro
                        formatado.append(str.substring(0, 2)).append("/")
                                .append(str.substring(2, 4)).append("/")
                                .append(str.substring(4, Math.min(len, 8)));
                    }
                }

                isUpdating = true;
                edtBirthday.setText(formatado.toString());
                edtBirthday.setSelection(formatado.length());
            }
        });
    }

    private void configurarMascaraCpf() {
        edtCpf.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) { isUpdating = false; return; }

                String str = s.toString().replaceAll("[^0-9]", "");
                StringBuilder formatado = new StringBuilder();

                int len = str.length();
                if (len > 0) {
                    if (len <= 3) {
                        formatado.append(str);
                    } else if (len <= 6) {
                        formatado.append(str.substring(0, 3)).append(".").append(str.substring(3));
                    } else if (len <= 9) {
                        formatado.append(str.substring(0, 3)).append(".")
                                .append(str.substring(3, 6)).append(".")
                                .append(str.substring(6));
                    } else {
                        formatado.append(str.substring(0, 3)).append(".")
                                .append(str.substring(3, 6)).append(".")
                                .append(str.substring(6, 9)).append("-")
                                .append(str.substring(9, Math.min(len, 11)));
                    }
                }

                isUpdating = true;
                edtCpf.setText(formatado.toString());
                edtCpf.setSelection(formatado.length());
            }
        });
    }
}