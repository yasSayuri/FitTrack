package com.fittrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.Treino;
import com.fittrack.entidades.User;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Home extends AppCompatActivity {

    private TextView txtTotalHoras, txtTotalTreinos, txtDiasTreinados, txtNomeHome;
    private View barDom, barSeg, barTer, barQua, barQui, barSex, barSab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        txtTotalHoras = findViewById(R.id.txtTotalHoras);
        txtTotalTreinos = findViewById(R.id.txtTotalTreinos);
        txtDiasTreinados = findViewById(R.id.txtDiasTreinados);
        txtNomeHome = findViewById(R.id.txtBoasVindas);

        barDom = findViewById(R.id.barDom);
        barSeg = findViewById(R.id.barSeg);
        barTer = findViewById(R.id.barTer);
        barQua = findViewById(R.id.barQua);
        barQui = findViewById(R.id.barQui);
        barSex = findViewById(R.id.barSex);
        barSab = findViewById(R.id.barSab);

        findViewById(R.id.btnNovoTreino).setOnClickListener(v -> startActivity(new Intent(Home.this, NovoTreino.class)));

        setupNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarDashboard();
        carregarNomeUsuario();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void carregarNomeUsuario() {
        new Thread(() -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            int userIdLogado = prefs.getInt("userId", -1);

            if (userIdLogado == -1) return;

            try {
                AppDatabase db = AppDatabase.getInstance(this);
                User usuarioAtual = db.userDao().getUserById(userIdLogado);

                if (usuarioAtual != null) {
                    runOnUiThread(() -> {
                        String primeiroNome = usuarioAtual.nome != null ? usuarioAtual.nome.split(" ")[0] : "User";
                        if (txtNomeHome != null) {
                            txtNomeHome.setText("Olá, " + primeiroNome);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void atualizarDashboard() {
        new Thread(() -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            int userIdLogado = prefs.getInt("userId", -1);

            if (userIdLogado == -1) return;

            try {
                AppDatabase db = AppDatabase.getInstance(this);
                List<Treino> lista = db.treinoDao().getTreinosByUserId(userIdLogado);

                int totalTreinos = lista.size();
                int totalHoras = 0;
                Set<String> diasUnicos = new HashSet<>();
                Set<Integer> diasComTreino = new HashSet<>();

                for (Treino t : lista) {
                    try {
                        if (t.duracao != null) {
                            String apenasNumeros = t.duracao.replaceAll("[^0-9]", "");
                            if (!apenasNumeros.isEmpty()) {
                                totalHoras += Integer.parseInt(apenasNumeros);
                            }
                        }
                    } catch (Exception ignored) {}

                    try {
                        if (t.data != null && t.data.contains("/")) {
                            String[] partes = t.data.split("/");
                            if (partes.length == 3) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(Integer.parseInt(partes[2]), Integer.parseInt(partes[1]) - 1, Integer.parseInt(partes[0]));
                                diasComTreino.add(cal.get(Calendar.DAY_OF_WEEK) - 1);
                            }
                        }
                    } catch (Exception ignored) {}

                    if (t.data != null && !t.data.trim().isEmpty()) {
                        diasUnicos.add(t.data);
                    }
                }

                final int fTreinos = totalTreinos;
                final int fHoras = totalHoras;
                final int fDias = diasUnicos.size();

                runOnUiThread(() -> {
                    txtTotalTreinos.setText(String.valueOf(fTreinos));
                    txtTotalHoras.setText(fHoras + "h");
                    txtDiasTreinados.setText(String.valueOf(fDias));

                    View[] barras = {barDom, barSeg, barTer, barQua, barQui, barSex, barSab};
                    for (int i = 0; i < 7; i++) {
                        if (barras[i] != null) {
                            barras[i].getLayoutParams().height = dpToPx(diasComTreino.contains(i) ? 100 : 10);
                            barras[i].requestLayout();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void setupNav() {
        findViewById(R.id.nav_historico).setOnClickListener(v -> trocarFragmento(new Historico()));
        findViewById(R.id.nav_treinos).setOnClickListener(v -> trocarFragmento(new Treinos()));
        findViewById(R.id.nav_perfil).setOnClickListener(v -> trocarFragmento(new Perfil()));
        findViewById(R.id.nav_config).setOnClickListener(v -> trocarFragmento(new Configuracoes()));
        findViewById(R.id.nav_home).setOnClickListener(v -> {
            findViewById(R.id.headerHome).setVisibility(View.VISIBLE);
            findViewById(R.id.divisoriaHome).setVisibility(View.VISIBLE);
            findViewById(R.id.dashboardContainer).setVisibility(View.VISIBLE);
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            // Forçamos a atualização ao clicar no botão de Home!
            atualizarDashboard();
            carregarNomeUsuario();
        });
    }

    private void trocarFragmento(Fragment fragmento) {
        findViewById(R.id.headerHome).setVisibility(View.GONE);
        findViewById(R.id.divisoriaHome).setVisibility(View.GONE);
        findViewById(R.id.dashboardContainer).setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmento).commit();
    }
}