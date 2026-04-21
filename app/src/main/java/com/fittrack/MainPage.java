package com.fittrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        Button btnLogin = findViewById(R.id.btnLoginMain);
        Button btnSignup = findViewById(R.id.btnSignupMain);

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, Login.class);
            startActivity(intent);
        });

        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, Cadastro.class);
            startActivity(intent);
        });
    }
}