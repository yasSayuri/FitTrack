package com.fittrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.Treino;
import java.util.List;

public class Historico extends Fragment {

    private LinearLayout containerCards;
    private LayoutInflater localInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.historico, container, false);
        localInflater = inflater;
        containerCards = view.findViewById(R.id.containerCards);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (localInflater != null && containerCards != null) {
            carregarTreinos(localInflater, containerCards);
        }
    }

    private void carregarTreinos(LayoutInflater inflater, LinearLayout container) {
        new Thread(() -> {
            if (getContext() == null) return;
            SharedPreferences prefs = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            int userIdLogado = prefs.getInt("userId", -1);

            if (userIdLogado == -1) return;

            AppDatabase db = AppDatabase.getInstance(requireContext());
            List<Treino> lista = db.treinoDao().getTreinosByUserId(userIdLogado);

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    container.removeAllViews();
                    for (Treino t : lista) {
                        View card = inflater.inflate(R.layout.item_treino, container, false);

                        TextView txtTipo = card.findViewById(R.id.txtTipoCard);
                        TextView txtData = card.findViewById(R.id.txtDataCard);
                        TextView txtDuracao = card.findViewById(R.id.txtDuracaoCard);

                        String durFormatada = "0h 0m";
                        if (t.duracao != null && t.duracao.contains(":")) {
                            try {
                                String[] p = t.duracao.split(":");
                                durFormatada = p[0] + "h " + p[1] + "m";
                            } catch (Exception ignored) {}
                        }

                        txtTipo.setText(t.tipo != null ? t.tipo : "");
                        txtData.setText(t.data != null ? t.data : "");
                        txtDuracao.setText(durFormatada);

                        card.setOnClickListener(v -> {
                            Intent intent = new Intent(requireContext(), DetalheTreino.class);
                            intent.putExtra("id", t.id);
                            intent.putExtra("tipo", t.tipo);
                            intent.putExtra("data", t.data);
                            intent.putExtra("duracao", t.duracao);
                            intent.putExtra("calorias", t.calorias);
                            intent.putExtra("descricao", t.descricao);
                            startActivity(intent);
                        });

                        container.addView(card);
                    }
                });
            }
        }).start();
    }
}