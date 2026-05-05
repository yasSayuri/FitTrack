package com.fittrack;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.User;

public class Home extends AppCompatActivity {

    private LinearLayout headerHome;
    private View divisoriaHome;
    private LinearLayout dashboardContainer;
    private ImageView navHistorico, navTreinos, navHome, navPerfil, navConfig;
    private TextView txtBoasVindas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        headerHome = findViewById(R.id.headerHome);
        divisoriaHome = findViewById(R.id.divisoriaHome);
        dashboardContainer = findViewById(R.id.dashboardContainer);
        navHistorico = findViewById(R.id.nav_historico);
        navTreinos = findViewById(R.id.nav_treinos);
        navHome = findViewById(R.id.nav_home);
        navPerfil = findViewById(R.id.nav_perfil);
        navConfig = findViewById(R.id.nav_config);
        txtBoasVindas = findViewById(R.id.txtBoasVindas);

        verificarPrimeiroAcesso();
        buscarNomeUsuario();

        Button btnNovoTreino = findViewById(R.id.btnNovoTreino);
        navHome.post(() -> atualizarIcones(navHome));

        btnNovoTreino.setOnClickListener(v -> {
            Toast.makeText(this, "Ainda não implementado", Toast.LENGTH_SHORT).show();
        });

        navHistorico.setOnClickListener(v -> {
            atualizarIcones(navHistorico);
            trocarFragmento(new Historico(), false);
        });

        navTreinos.setOnClickListener(v -> {
            atualizarIcones(navTreinos);
            trocarFragmento(new Treinos(), false);
        });

        navHome.setOnClickListener(v -> {
            atualizarIcones(navHome);
            headerHome.setVisibility(View.VISIBLE);
            divisoriaHome.setVisibility(View.VISIBLE);
            dashboardContainer.setVisibility(View.VISIBLE);

            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });

        navPerfil.setOnClickListener(v -> {
            atualizarIcones(navPerfil);
            trocarFragmento(new Perfil(), false);
        });

        navConfig.setOnClickListener(v -> {
            atualizarIcones(navConfig);
            trocarFragmento(new Configuracoes(), false);
        });
    }

    private void verificarPrimeiroAcesso() {
        SharedPreferences pref = getSharedPreferences("FitTrackPrefs", MODE_PRIVATE);
        boolean primeiroAcesso = pref.getBoolean("primeiro_acesso", true);

        if (primeiroAcesso) {
            mostrarPopUpOnboarding();
            pref.edit().putBoolean("primeiro_acesso", false).apply();
        }
    }

    private void buscarNomeUsuario() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            User user = db.userDao().getLastUser();

            if (user != null && user.nome != null) {
                String primeiroNome = user.nome.split(" ")[0];
                String textoCompleto = "Olá, " + primeiroNome;

                SpannableString spannable = new SpannableString(textoCompleto);
                int corRoxa = ContextCompat.getColor(this, R.color.purple_primary);

                spannable.setSpan(
                        new ForegroundColorSpan(corRoxa),
                        5,
                        textoCompleto.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                runOnUiThread(() -> txtBoasVindas.setText(spannable));
            }
        }).start();
    }

    private void atualizarIcones(ImageView selecionado) {
        ImageView[] icones = {navHistorico, navTreinos, navHome, navPerfil, navConfig};
        int corInativo = ContextCompat.getColor(this, R.color.text_hint);
        int corAtivo = ContextCompat.getColor(this, R.color.purple_primary);

        for (ImageView img : icones) {
            img.setColorFilter(corInativo);
        }

        selecionado.setColorFilter(corAtivo);
    }

    private void trocarFragmento(Fragment fragmento, boolean mostrarHeader) {
        if (mostrarHeader) {
            headerHome.setVisibility(View.VISIBLE);
            divisoriaHome.setVisibility(View.VISIBLE);
            dashboardContainer.setVisibility(View.VISIBLE);
        } else {
            headerHome.setVisibility(View.GONE);
            divisoriaHome.setVisibility(View.GONE);
            dashboardContainer.setVisibility(View.GONE);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragmento)
                .commit();
    }

    private void mostrarPopUpOnboarding() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_onboarding);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        LinearLayout layoutInicial = dialog.findViewById(R.id.layoutInicial);
        LinearLayout layoutFormulario = dialog.findViewById(R.id.layoutFormulario);
        Button btnDeixaPraDepois = dialog.findViewById(R.id.btnDeixaPraDepois);
        Button btnVamos = dialog.findViewById(R.id.btnVamos);
        Button btnSalvarTreino = dialog.findViewById(R.id.btnSalvarTreino);

        btnDeixaPraDepois.setOnClickListener(v -> dialog.dismiss());
        btnVamos.setOnClickListener(v -> {
            layoutInicial.setVisibility(View.GONE);
            layoutFormulario.setVisibility(View.VISIBLE);
        });
        btnSalvarTreino.setOnClickListener(v -> {
            Toast.makeText(this, "Treino cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();

        if (dialog.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }
}