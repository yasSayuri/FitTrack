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

public class Login extends AppCompatActivity {

    private EditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        ImageView btnVoltar = findViewById(R.id.btnVoltarLogin);
        Button btnDoLogin = findViewById(R.id.btnDoLogin);
        TextView txtIrParaCadastro = findViewById(R.id.txtIrParaCadastro);

        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, MainPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        btnDoLogin.setOnClickListener(v -> realizarLogin());

        txtIrParaCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Cadastro.class);
            startActivity(intent);
        });
    }

    private void realizarLogin() {
        String email = edtEmail.getText().toString().trim();
        String senha = edtPassword.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Formato de e-mail inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            User user = db.userDao().login(email, senha);

            runOnUiThread(() -> {
                if (user != null) {
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "E-mail ou senha incorretos", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}