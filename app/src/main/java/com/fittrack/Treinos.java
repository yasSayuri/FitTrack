package com.fittrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.TreinoPlano;

import java.util.List;

public class Treinos extends Fragment {

    private LinearLayout containerCards;
    private LayoutInflater localInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.treinos, container, false);

        localInflater = inflater;

        Button btnCriarTreino = view.findViewById(R.id.btnCriarTreino);
        containerCards = view.findViewById(R.id.containerCardsTreinos);

        btnCriarTreino.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), CriarTreino.class));
        });

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
        if (getActivity() == null) return;

        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userIdLogado = prefs.getInt("userId", -1);

        if (userIdLogado == -1) return;

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            List<TreinoPlano> treinos = db.treinoPlanoDao().getTreinosPlanosByUserId(userIdLogado);

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    container.removeAllViews();

                    for (TreinoPlano t : treinos) {
                        View card = inflater.inflate(R.layout.item_treino_plano, container, false);

                        TextView txtTipo = card.findViewById(R.id.txtTipoTreinoPlano);

                        txtTipo.setText(t.tipo != null ? t.tipo : "Treino");

                        card.setOnClickListener(c -> {
                            Intent intent = new Intent(requireContext(), DetalheTreinoPlano.class);
                            intent.putExtra("id", t.id);
                            startActivity(intent);
                        });

                        container.addView(card);
                    }
                });
            }
        }).start();
    }
}