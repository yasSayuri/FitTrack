package com.fittrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.User;

public class Onboarding extends AppCompatActivity {

    private LinearLayout telaIntro;
    private LinearLayout telaGenero;
    private LinearLayout telaDados;
    private LinearLayout telaObjetivo;
    private LinearLayout telaFinal;

    private Button btnMasculino;
    private Button btnFeminino;

    private Button btnAtividadeLeve;
    private Button btnAtividadeModerada;
    private Button btnAtividadeIntensa;

    private EditText edtPeso;
    private EditText edtAltura;
    private EditText edtObjetivo;

    private String generoSelecionado = "Feminino";
    private String atividadeSelecionada = "Leve";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding);

        telaIntro = findViewById(R.id.telaIntro);
        telaGenero = findViewById(R.id.telaGenero);
        telaDados = findViewById(R.id.telaDados);
        telaObjetivo = findViewById(R.id.telaObjetivo);
        telaFinal = findViewById(R.id.telaFinal);

        edtPeso = findViewById(R.id.edtPeso);
        edtAltura = findViewById(R.id.edtAltura);
        edtObjetivo = findViewById(R.id.edtObjetivo);

        Button btnComecarJornada = findViewById(R.id.btnComecarJornada);

        Button btnVoltarGenero = findViewById(R.id.btnVoltarGenero);
        Button btnProximoGenero = findViewById(R.id.btnProximoGenero);

        Button btnVoltarDados = findViewById(R.id.btnVoltarDados);
        Button btnProximoDados = findViewById(R.id.btnProximoDados);

        Button btnVoltarObjetivo = findViewById(R.id.btnVoltarObjetivo);
        Button btnProximoObjetivo = findViewById(R.id.btnProximoObjetivo);

        Button btnFinalizarOnboarding = findViewById(R.id.btnFinalizarOnboarding);

        btnMasculino = findViewById(R.id.btnMasculino);
        btnFeminino = findViewById(R.id.btnFeminino);

        btnAtividadeLeve = findViewById(R.id.btnAtividadeLeve);
        btnAtividadeModerada = findViewById(R.id.btnAtividadeModerada);
        btnAtividadeIntensa = findViewById(R.id.btnAtividadeIntensa);

        btnComecarJornada.setOnClickListener(v -> mostrarTela(telaGenero));

        btnVoltarGenero.setOnClickListener(v -> mostrarTela(telaIntro));
        btnProximoGenero.setOnClickListener(v -> mostrarTela(telaDados));

        btnVoltarDados.setOnClickListener(v -> mostrarTela(telaGenero));

        btnProximoDados.setOnClickListener(v -> {
            if (validarDados()) {
                mostrarTela(telaObjetivo);
            }
        });

        btnVoltarObjetivo.setOnClickListener(v -> mostrarTela(telaDados));

        btnProximoObjetivo.setOnClickListener(v -> {
            if (validarObjetivo()) {
                mostrarTela(telaFinal);
            }
        });

        btnFinalizarOnboarding.setOnClickListener(v -> salvarDados());

        btnMasculino.setOnClickListener(v -> {
            generoSelecionado = "Masculino";
            atualizarToggleGenero();
        });

        btnFeminino.setOnClickListener(v -> {
            generoSelecionado = "Feminino";
            atualizarToggleGenero();
        });

        btnAtividadeLeve.setOnClickListener(v -> {
            atividadeSelecionada = "Leve";
            atualizarToggleAtividade();
        });

        btnAtividadeModerada.setOnClickListener(v -> {
            atividadeSelecionada = "Moderada";
            atualizarToggleAtividade();
        });

        btnAtividadeIntensa.setOnClickListener(v -> {
            atividadeSelecionada = "Intensa";
            atualizarToggleAtividade();
        });

        atualizarToggleGenero();
        atualizarToggleAtividade();
    }

    private void mostrarTela(LinearLayout telaSelecionada) {
        telaIntro.setVisibility(View.GONE);
        telaGenero.setVisibility(View.GONE);
        telaDados.setVisibility(View.GONE);
        telaObjetivo.setVisibility(View.GONE);
        telaFinal.setVisibility(View.GONE);

        telaSelecionada.setVisibility(View.VISIBLE);
    }

    private boolean validarDados() {
        String peso = edtPeso.getText().toString().trim();
        String altura = edtAltura.getText().toString().trim();

        if (peso.isEmpty() || altura.isEmpty()) {
            Toast.makeText(this, "Informe seu peso e sua altura!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean validarObjetivo() {
        String objetivo = edtObjetivo.getText().toString().trim();

        if (objetivo.isEmpty()) {
            Toast.makeText(this, "Informe seu objetivo!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void atualizarToggleGenero() {
        if (generoSelecionado.equals("Masculino")) {
            selecionarBotao(btnMasculino);
            desselecionarBotao(btnFeminino);
        } else {
            selecionarBotao(btnFeminino);
            desselecionarBotao(btnMasculino);
        }
    }

    private void atualizarToggleAtividade() {
        desselecionarBotao(btnAtividadeLeve);
        desselecionarBotao(btnAtividadeModerada);
        desselecionarBotao(btnAtividadeIntensa);

        if (atividadeSelecionada.equals("Leve")) {
            selecionarBotao(btnAtividadeLeve);
        } else if (atividadeSelecionada.equals("Moderada")) {
            selecionarBotao(btnAtividadeModerada);
        } else {
            selecionarBotao(btnAtividadeIntensa);
        }
    }

    private void selecionarBotao(Button botao) {
        botao.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_primary)));
        botao.setTextColor(getResources().getColor(R.color.text_white));
    }

    private void desselecionarBotao(Button botao) {
        botao.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_dark)));
        botao.setTextColor(getResources().getColor(R.color.text_hint));
    }

    private void salvarDados() {
        String peso = edtPeso.getText().toString().trim();
        String altura = edtAltura.getText().toString().trim();
        String objetivo = edtObjetivo.getText().toString().trim();

        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userIdLogado = prefs.getInt("userId", -1);

        if (userIdLogado == -1) {
            Toast.makeText(this, "Erro de sessão!", Toast.LENGTH_SHORT).show();
            return;
        }

        prefs.edit()
                .putString("genero", generoSelecionado)
                .putString("objetivo", objetivo)
                .putString("atividade", atividadeSelecionada)
                .apply();

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(Onboarding.this);
            User user = db.userDao().getUserById(userIdLogado);

            if (user != null) {
                user.peso = peso;
                user.altura = altura;

                db.userDao().update(user);
            }

            runOnUiThread(() -> {
                Toast.makeText(Onboarding.this, "Perfil criado com sucesso!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Onboarding.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                finish();
            });
        }).start();
    }
}