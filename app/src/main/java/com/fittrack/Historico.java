package com.fittrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.Treino;
import java.util.List;

public class Historico extends Fragment {

    private int lastScrollY = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.historico, container, false);

        ImageView btnVoltar = view.findViewById(R.id.btnVoltar);
        NestedScrollView scroll = view.findViewById(R.id.scrollHistorico);
        LinearLayout containerCards = view.findViewById(R.id.containerCards);

        btnVoltar.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().findViewById(R.id.nav_home).performClick();
            }
        });

        scroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (getActivity() instanceof Home) {
                View bottomBar = getActivity().findViewById(R.id.bottomBarContainer);
                if (bottomBar != null) {
                    if (scrollY > lastScrollY) {
                        bottomBar.animate().translationY(bottomBar.getHeight()).setDuration(200);
                    } else if (scrollY < lastScrollY) {
                        bottomBar.animate().translationY(0).setDuration(200);
                    }
                }
                lastScrollY = scrollY;
            }
        });

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            List<Treino> treinos = db.treinoDao().getAllTreinos();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    containerCards.removeAllViews();
                    for (Treino t : treinos) {
                        View card = inflater.inflate(R.layout.item_treino, containerCards, false);

                        TextView txtTipo = card.findViewById(R.id.txtTipoCard);
                        TextView txtDuracao = card.findViewById(R.id.txtDuracaoCard);
                        TextView txtDescricao = card.findViewById(R.id.txtDescricaoCard);

                        txtTipo.setText(t.tipo);
                        txtDuracao.setText("Duração: " + t.duracao + " | Data: " + t.data);
                        txtDescricao.setText(t.descricao);

                        containerCards.addView(card);
                    }
                });
            }
        }).start();

        return view;
    }
}