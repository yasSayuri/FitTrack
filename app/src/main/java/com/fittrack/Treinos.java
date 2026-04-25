package com.fittrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class Treinos extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.treinos, container, false);

        ImageView btnVoltar = view.findViewById(R.id.btnVoltar);
        Button btnCriarTreino = view.findViewById(R.id.btnCriarTreino);
        CardView cardAcademia = view.findViewById(R.id.cardAcademia);
        CardView cardCardio = view.findViewById(R.id.cardCardio);
        CardView cardCrossfit = view.findViewById(R.id.cardCrossfit);

        btnVoltar.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().findViewById(R.id.nav_home).performClick();
            }
        });

        View.OnClickListener listenerNaoImplementado = v -> {
            Toast.makeText(getContext(), "Ainda não implementado", Toast.LENGTH_SHORT).show();
        };

        btnCriarTreino.setOnClickListener(listenerNaoImplementado);
        cardAcademia.setOnClickListener(listenerNaoImplementado);
        cardCardio.setOnClickListener(listenerNaoImplementado);
        cardCrossfit.setOnClickListener(listenerNaoImplementado);

        return view;
    }
}