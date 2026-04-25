package com.fittrack;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class Home extends AppCompatActivity {

    private LinearLayout headerHome;
    private View divisoriaHome;
    private ImageView navHistorico, navTreinos, navHome, navPerfil, navConfig;
    private View bolaRoxa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mostrarPopUpOnboarding();

        headerHome = findViewById(R.id.headerHome);
        divisoriaHome = findViewById(R.id.divisoriaHome);
        navHistorico = findViewById(R.id.nav_historico);
        navTreinos = findViewById(R.id.nav_treinos);
        navHome = findViewById(R.id.nav_home);
        navPerfil = findViewById(R.id.nav_perfil);
        navConfig = findViewById(R.id.nav_config);
        bolaRoxa = findViewById(R.id.bolaRoxa);

        navHome.post(() -> moverBolaRoxa(navHome));

        navHistorico.setOnClickListener(v -> {
            moverBolaRoxa(navHistorico);
            trocarFragmento(new Historico(), false);
        });

        navTreinos.setOnClickListener(v -> {
            moverBolaRoxa(navTreinos);
            trocarFragmento(new Treinos(), false);
        });

        navHome.setOnClickListener(v -> {
            moverBolaRoxa(navHome);
            headerHome.setVisibility(View.VISIBLE);
            divisoriaHome.setVisibility(View.VISIBLE);
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });

        navPerfil.setOnClickListener(v -> {
            moverBolaRoxa(navPerfil);
            trocarFragmento(new Perfil(), false);
        });

        navConfig.setOnClickListener(v -> {
            moverBolaRoxa(navConfig);
            trocarFragmento(new Configuracoes(), false);
        });
    }

    private void moverBolaRoxa(ImageView iconeSelecionado) {
        float targetX = iconeSelecionado.getX() + (iconeSelecionado.getWidth() / 2f) - (bolaRoxa.getWidth() / 2f);
        float targetY = iconeSelecionado.getY() + (iconeSelecionado.getHeight() / 2f) - (bolaRoxa.getHeight() / 2f);

        bolaRoxa.animate()
                .x(targetX)
                .y(targetY)
                .setDuration(300)
                .start();

        navHome.animate()
                .translationY(-15)
                .setDuration(300)
                .start();

        ImageView[] icones = {navHistorico, navTreinos, navHome, navPerfil, navConfig};
        for (ImageView img : icones) {
            img.setColorFilter(ContextCompat.getColor(this, R.color.text_hint));
        }

        iconeSelecionado.setColorFilter(ContextCompat.getColor(this, R.color.text_white));
    }

    private void trocarFragmento(Fragment fragmento, boolean mostrarHeader) {
        if (mostrarHeader) {
            headerHome.setVisibility(View.VISIBLE);
            divisoriaHome.setVisibility(View.VISIBLE);
        } else {
            headerHome.setVisibility(View.GONE);
            divisoriaHome.setVisibility(View.GONE);
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