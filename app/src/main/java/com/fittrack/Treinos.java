package com.fittrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.TreinoPlano;
import java.util.List;

public class Treinos extends Fragment {

    private LinearLayout containerCards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.treinos, container, false);

        ImageView btnVoltar = view.findViewById(R.id.btnVoltar);
        Button btnCriarTreino = view.findViewById(R.id.btnCriarTreino);
        containerCards = view.findViewById(R.id.containerCardsTreinos);

        btnVoltar.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().findViewById(R.id.nav_home).performClick();
            }
        });

        btnCriarTreino.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), CriarTreino.class));
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarTreinos();
    }

    private void carregarTreinos() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            List<TreinoPlano> treinos = db.treinoPlanoDao().getAllTreinosPlanos();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    containerCards.removeAllViews();
                    LayoutInflater inflater = LayoutInflater.from(requireContext());

                    for (TreinoPlano t : treinos) {
                        View card = inflater.inflate(R.layout.item_treino_plano, containerCards, false);

                        TextView txtTipo = card.findViewById(R.id.txtTipoTreinoPlano);
                        TextView txtDetalhes = card.findViewById(R.id.txtDetalhesTreinoPlano);

                        txtTipo.setText(t.tipo);
                        txtDetalhes.setText(t.exercicios + " • " + t.series + " Séries de " + t.repeticoes);

                        card.setOnClickListener(c -> {
                            Toast.makeText(requireContext(), "Ainda não implementado", Toast.LENGTH_SHORT).show();
                        });

                        containerCards.addView(card);
                    }
                });
            }
        }).start();
    }
}